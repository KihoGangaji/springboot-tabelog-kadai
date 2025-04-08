package com.example.nagoyameshi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripeService {
    
    @Value("${stripe.secret-key}")
    private String stripeApiKey;
    
    public String createSubscriptionSession(Long userId) {
        Stripe.apiKey = stripeApiKey;

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("jpy")
                                .setUnitAmount(1000L) // 例: ¥1000のサブスクリプション
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("有料会員プラン")
                                        .build()
                                )
                                .build()
                        )
                        .setQuantity(1L)
                        .build()
                )
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION) // 定期課金
                .setSuccessUrl("http://localhost:8080/stripe/success?userId=" + userId) // 成功後のURL
                .setCancelUrl("http://localhost:8080/stripe/cancel") // キャンセル時のURL
                .putAllMetadata(java.util.Map.of("userId", userId.toString())) // ユーザーIDを保存
                .build();

            Session session = Session.create(params);
            return session.getId();
        } catch (StripeException e) {
            e.printStackTrace();
            return "";
        }
    }
}
