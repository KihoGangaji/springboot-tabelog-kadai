package com.example.nagoyameshi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Shop;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.ShopRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;
import com.example.nagoyameshi.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReservationController {
    private static final Integer reserveCnt = null;
	private final ReservationRepository reservationRepository;   
    private final ShopRepository shopRepository;
    private final ReservationService reservationService; 
    private final StripeService stripeService;
    
    public ReservationController(ReservationRepository reservationRepository, ShopRepository shopRepository, ReservationService reservationService, StripeService stripeService) { 
    	this.reservationRepository = reservationRepository; 
        this.shopRepository = shopRepository;
        this.reservationService = reservationService;
        this.stripeService = stripeService;
    }

    @GetMapping("/reservations")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
        User user = userDetailsImpl.getUser();
        Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        model.addAttribute("reservationPage", reservationPage);
        return "reservations/index";
    }

    @GetMapping("/shops/{id}/reservations/create")
    public String createReservation(@PathVariable(name = "id") Integer id, Model model) {
        Shop shop = shopRepository.findById(id).orElse(null);
        if (shop == null) {
            model.addAttribute("errorMessage", "店舗情報が見つかりません。");
            return "error"; // エラーページにリダイレクト
        }

        model.addAttribute("shop", shop);
        model.addAttribute("reservationInputForm", new ReservationInputForm());

        return "reservations/create";
    }

    @GetMapping("/shops/{id}/reservations/input")
    public String input(@PathVariable(name = "id") Integer id,
                        @ModelAttribute @Validated ReservationInputForm reservationInputForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model)
    {
    	Shop shop = shopRepository.getReferenceById(id);
    	Integer numberOfPeople = reservationInputForm.getNumberOfPeople();
    	
    	if (numberOfPeople != null) {
            if (!reservationService.isWithinCapacity(numberOfPeople, reserveCnt)) {
                FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numberOfPeople", "予約人数が定員を超えています。");
                bindingResult.addError(fieldError);                
            }            
        }
    	
        if (bindingResult.hasErrors()) {            
            model.addAttribute("shop", shop);            
            model.addAttribute("errorMessage", "予約内容に不備があります。"); 
            return "shops/show";
        }
        
        redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);
        
        return "redirect:/shops/{id}/reservations/confirm";
    }

    @GetMapping("/shops/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
                          @ModelAttribute ReservationInputForm reservationInputForm,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                          HttpServletRequest httpServletRequest,
                          Model model) 
    {
        Shop shop = shopRepository.getReferenceById(id);
        User user = userDetailsImpl.getUser();
        
        new ReservationRegisterForm(shop.getId(), user.getId(), reservationInputForm.getReserveDateTime(), reservationInputForm.getNumberOfPeople());
        

        // モデルに追加
        model.addAttribute("shop", shop);
        model.addAttribute("reservationRegisterForm", reservationInputForm); 

        return "reservations/confirm";
    }
    
    @PostMapping("/shops/{id}/reservations/create")
    public String create(@PathVariable(name = "id") Integer id,
                         @ModelAttribute ReservationRegisterForm reservationRegisterForm,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,HttpSession session,
                         RedirectAttributes redirectAttributes) {
    	// セッションからuserIdとshopIdを取得
        User user = userDetailsImpl.getUser();
        //Integer shopId = (Integer) session.getAttribute("shopId"); // shopIdを取得
        
        Shop shop = shopRepository.getReferenceById(id); // Shopを取得
      

        // 予約オブジェクトを作成
        Reservation reservation = new Reservation();
        reservation.setUser(user); // ユーザーを設定
        reservation.setShop(shop);
        
        // 日時の文字列をLocalDateTimeに変換
        String reserveDateTimeStr = reservationRegisterForm.getReserveDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime reserveDateTime = LocalDateTime.parse(reserveDateTimeStr, formatter);
        
        // 変換したLocalDateTimeを設定
        reservation.setReserveDateTime(reserveDateTime);
        reservation.setNumberOfPeople(reservationRegisterForm.getNumberOfPeople()); 
        
        // 予約を保存
        reservationRepository.save(reservation);

        redirectAttributes.addFlashAttribute("successMessage", "予約が完了しました。");

        return "redirect:/reservations?reserved";
    }

    @PostMapping("/reservations/{id}/cancel")
    public String cancel(@PathVariable(name = "id") Integer id,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                         RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);

        if (reservation == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "予約が見つかりません。");
            return "redirect:/reservations";
        }

        // Check if the reservation belongs to the current user
        if (!reservation.getUser().getId().equals(userDetailsImpl.getUser().getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "この予約をキャンセルする権限がありません。");
            return "redirect:/reservations";
        }

        // Check if the reservation is in the future
//        if (reservation.getReserveDateTime().isBefore(LocalDateTime.now())) {
//            redirectAttributes.addFlashAttribute("errorMessage", "過去の予約はキャンセルできません。");
//            return "redirect:/reservations";
//        }

        reservationRepository.delete(reservation);
        redirectAttributes.addFlashAttribute("successMessage", "予約をキャンセルしました。");
        return "redirect:/reservations";
    }
}
