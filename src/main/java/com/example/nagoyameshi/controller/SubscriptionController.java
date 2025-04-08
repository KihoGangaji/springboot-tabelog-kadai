package com.example.nagoyameshi.controller;

import com.example.nagoyameshi.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.nagoyameshi.service.SubscriptionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.nagoyameshi.repository.UserRepository;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository; // 追加

    public SubscriptionController(SubscriptionService subscriptionService, UserRepository userRepository) {
        this.subscriptionService = subscriptionService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showSubscriptionPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails == null) {
            model.addAttribute("errorMessage", "ログインしてください。");
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));


        boolean hasSubscription = subscriptionService.hasActiveSubscription(user);


        if (hasSubscription) {
            return "redirect:/subscription/cancel";
        }



        return "subscription";  // resources/templates/subscription.html が必要
    }

    @GetMapping("/cancel")
    public String showCancelSubscriptionPage(Model model) {
        // successMessageをモデルに追加
        String successMessage = "有料プランに登録できました！";  // ここでメッセージを設定（条件によって変更可能）
        model.addAttribute("successMessage", successMessage);

        return "subscription/cancel";
    }
}
