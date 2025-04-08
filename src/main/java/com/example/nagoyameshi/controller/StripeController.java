package com.example.nagoyameshi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.nagoyameshi.service.UserService;

@RestController
public class StripeController {

    @Autowired
    private UserService userService;

    @GetMapping("/stripe/success")
    public String stripeSuccess(@RequestParam Long userId) {
        // userService.updateUserRole(userId, 2); // role_id = 2（有料会員）に更新
        return "支払いが成功しました！";
    }
}
