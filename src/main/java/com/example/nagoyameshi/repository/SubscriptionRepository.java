package com.example.nagoyameshi.repository;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>  {
    Subscription findByUser(User user);

    boolean existsByUserAndIsActive(User user, Boolean isActive);

}
