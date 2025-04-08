package com.example.nagoyameshi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeConfig {
	
	@Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.public-key}")
    private String publicKey;

    public String getSecretKey() {
        return secretKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

}
