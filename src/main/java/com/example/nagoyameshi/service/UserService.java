package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StripeService stripeService;

    public UserService(UserRepository userRepository, 
                       RoleRepository roleRepository, 
                       PasswordEncoder passwordEncoder, 
                       StripeService stripeService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.stripeService = stripeService;
    }
    
    @Transactional
    public void registerForPremium(User user) {
        try {
            // stripeService.createSubscription(user);
            // user.setSubscriptionStatus("ACTIVE");

            // `ROLE_PREMIUM` のロールを取得
            Role premiumRole = roleRepository.findByName("ROLE_PREMIUM")
                .orElseThrow(() -> new RuntimeException("Role not found"));

            // `role_id` をプレミアム会員のものに更新
            user.setRole(premiumRole);
            
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("サブスクリプションの登録に失敗しました", e);
        }
    }


    @Transactional
    public void cancelSubscription(User user) {
        try {
            //stripeService.cancelSubscription(user);
            // user.setSubscriptionStatus("CANCELLED");

            // `ROLE_GENERAL` に戻す
            Role generalRole = roleRepository.findByName("ROLE_GENERAL")
                .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(generalRole);

            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("サブスクリプションの解約に失敗しました", e);
        }
    }

    
    @Transactional
    public User create(SignupForm signupForm) {
        // ロールを取得する際にOptionalを扱う
        Role role = roleRepository.findByName("ROLE_GENERAL")
                             .orElseThrow(() -> new RuntimeException("Role not found"));
        
        User user = new User();
        user.setName(signupForm.getName());
        user.setFurigana(signupForm.getFurigana());
        user.setAddress(signupForm.getAddress());
        user.setPhoneNumber(signupForm.getPhoneNumber());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setRole(role);
        user.setEnabled(false);

        return userRepository.save(user);
    }

    @Transactional
    public void update(UserEditForm userEditForm) {
        User user = userRepository.getReferenceById(userEditForm.getId());
        
        user.setName(userEditForm.getName());
        user.setFurigana(userEditForm.getFurigana());
        user.setAddress(userEditForm.getAddress());
        user.setPhoneNumber(userEditForm.getPhoneNumber());
        user.setEmail(userEditForm.getEmail());      
        
        userRepository.save(user);
    } 

    public boolean isEmailRegistered(String email) {
        Optional<User> user = userRepository.findByEmail(email);  
        return user.isPresent();
    }

    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    @Transactional
    public void enableUser(User user) {
        if (!user.getEnabled()) {
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    public boolean isEmailChanged(UserEditForm userEditForm) {
        User currentUser = userRepository.getReferenceById(userEditForm.getId());
        return !userEditForm.getEmail().equals(currentUser.getEmail());
    }

	public void subscribeToPremium(User user) {
		// TODO 自動生成されたメソッド・スタブ
		
	}  
}
