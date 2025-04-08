package com.example.nagoyameshi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.UserService;
import com.example.nagoyameshi.service.SubscriptionService;

	@Controller
	@RequestMapping("/user")
	public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public UserController(UserRepository userRepository, UserService userService,SubscriptionService subscriptionService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }    

    // ユーザーのプロフィール画面
    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {         
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        model.addAttribute("user", user);
        return "user/index";
    }
    
    // ユーザーの情報編集ページ
    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {        
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getFurigana(), user.getAddress(), user.getPhoneNumber(), user.getEmail());
        model.addAttribute("userEditForm", userEditForm);
        return "user/edit";
    }
    
    // 有料プラン登録ページ
    @GetMapping("/subscription")
    public String premium(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        model.addAttribute("user", user);  // ユーザー情報を渡す
        return "user/subscription";  // 有料プラン登録ページ
    }
    
    // 有料プラン登録処理
    @PostMapping("/subscription")
    public String subscribe(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes) {
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        
        subscriptionService.activateSubscription(user.getId());
        // 成功メッセージを設定
        redirectAttributes.addFlashAttribute("successMessage", "サブスクリプションが正常に取得されました！");

        return "redirect:/";  // リダイレクト先をホーム画面に設定
    }

        @PostMapping("/subscription/cancel")
        public String cancelSubscription(
                @AuthenticationPrincipal UserDetailsImpl userDetails,
                RedirectAttributes redirectAttributes) {

            User user = userRepository.getReferenceById(userDetails.getUser().getId());

            try {
                subscriptionService.cancelSubscription(user.getId());
                redirectAttributes.addFlashAttribute("successMessage", "有料プランの解約が完了しました");
            } catch (RuntimeException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "解約処理中にエラーが発生しました");
            }

            return "redirect:/subscription";
        }
        
    @PostMapping("/update")
    public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }

        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        userService.update(userEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        return "redirect:/user";
    }    
}
