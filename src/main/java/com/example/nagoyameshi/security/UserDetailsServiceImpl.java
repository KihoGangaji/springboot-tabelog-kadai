package com.example.nagoyameshi.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.service.SubscriptionService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    public UserDetailsServiceImpl(UserRepository userRepository, SubscriptionService subscriptionService) {
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Optional<User>を取得
        Optional<User> userOptional = userRepository.findByEmail(username);

        // Userを取得し、存在しない場合はUsernameNotFoundExceptionをスロー
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりませんでした。"));

        boolean hasActiveSubscription = subscriptionService.hasActiveSubscription(user);

        // ユーザーのロールを取得
        String userRoleName = user.getRole().getName();
        Collection<GrantedAuthority> authorities = new ArrayList<>();         
        authorities.add(new SimpleGrantedAuthority(userRoleName));
        
        // UserDetailsImplを返す
        return new UserDetailsImpl(user, authorities, hasActiveSubscription);
    }
}
