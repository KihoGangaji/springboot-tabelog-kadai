package com.example.nagoyameshi.service;
import org.springframework.stereotype.Service;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.repository.SubscriptionRepository;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.Subscription;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    public Subscription activateSubscription(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user already has a subscription
        Subscription existingSubscription = subscriptionRepository.findByUser(user);

        if (existingSubscription != null) {
            // Update existing subscription
            existingSubscription.setIsActive(true);
            return subscriptionRepository.save(existingSubscription);
        } else {
            // Create new subscription
            Subscription newSubscription = new Subscription();
            newSubscription.setUser(user);
            newSubscription.setIsActive(true);
            return subscriptionRepository.save(newSubscription);
        }
    }

    public boolean hasActiveSubscription(User user) {
        return subscriptionRepository.existsByUserAndIsActive(user, true);
    }

    public Subscription cancelSubscription(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subscription existingSubscription = subscriptionRepository.findByUser(user);
        if (existingSubscription == null) {
            throw new RuntimeException("Subscription not found for this user");
        }


        existingSubscription.setIsActive(false);
        return subscriptionRepository.save(existingSubscription);
    }
}
