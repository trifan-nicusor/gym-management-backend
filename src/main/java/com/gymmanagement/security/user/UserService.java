package com.gymmanagement.security.user;

import com.gymmanagement.security.email.EmailSender;
import com.gymmanagement.security.email.EmailValidator;
import com.gymmanagement.security.email.builder.EmailBuilderService;
import com.gymmanagement.security.token.reset.ResetToken;
import com.gymmanagement.security.token.reset.ResetTokenRepository;
import com.gymmanagement.security.token.reset.ResetTokenServiceImpl;
import com.gymmanagement.subscription.Subscription;
import com.gymmanagement.subscription.SubscriptionServiceImpl;
import com.gymmanagement.subscription.joinentity.UserSubscription;
import com.gymmanagement.subscription.joinentity.UserSubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final EmailSender emailSender;
    private final EmailBuilderService emailBuilderService;
    private final ResetTokenServiceImpl resetTokenService;
    private final SubscriptionServiceImpl subscriptionService;
    @Value("${domain}")
    private String domain;
    @Value("${uuid.token.expiration}")
    private int expireTime;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User loadByEmail(String email) {
        return userRepository.loadByEmail(email);
    }

    public void changePassword(UserDetails user, String password) {
        userRepository.updatePassword(user.getUsername(), passwordEncoder.encode(password));
    }

    public boolean checkIfPasswordMatches(UserDetails user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    public boolean userExists(String email) {
        return this.findByEmail(email).isPresent();
    }

    public boolean isEmailValid(String email) {
        return emailValidator.test(email);
    }

    @Transactional
    public void enableUser(User user) {
        userRepository.enableUser(user.getEmail());
    }

    public void sendPasswordResetEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        String token = UUID.randomUUID().toString();
        String link = domain + "/api/v1/auth/reset-password?resetToken=" + token;

        var resetToken = ResetToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(expireTime))
                .user(user)
                .build();

        resetTokenRepository.save(resetToken);

        emailSender.send(email, emailBuilderService.forgotPasswordEmailBuilder(user.getFirstName(), link));
    }

    public void resetPassword(ResetToken token, String password) {
        User user = token.getUser();
        resetTokenService.getUserLastToken(user.getId()).setExpiresAt(LocalDateTime.now());

        changePassword(user, password);
    }

    public boolean isUserEnabled(String email) {
        return userRepository.isUserEnabled(email);
    }

    public void addUserSubscription(String email, SubscriptionRequest request) {
        User user = loadByEmail(email);
        List<Subscription> subscriptionList = new ArrayList<>();
        List<Long> currentSubscription = userSubscriptionRepository.getAllCurrentSubscriptions(user.getId());

        currentSubscription.forEach(id -> request.getSubscriptionList().add(id));

        request.getSubscriptionList().forEach(id -> {
            Subscription subscription = subscriptionService.loadSubscriptionById(id.intValue());
            subscriptionList.add(subscription);
        });

        user.setSubscriptionList(subscriptionList);

        userRepository.save(user);

        request.getSubscriptionList().forEach(id -> {
            Subscription subscription = subscriptionService.loadSubscriptionById(id.intValue());
            setSubscriptionExpire(user.getId(), id, subscription.getDuration());
        });
    }

    private void setSubscriptionExpire(Long userId, Long subscriptionId, int duration) {
        UserSubscription userSubscription = userSubscriptionRepository.findById(userId, subscriptionId);

        if (userSubscription.getExpiresAt() == null) {
            userSubscription.setExpiresAt(LocalDateTime.now().plusDays(duration));

            userSubscriptionRepository.save(userSubscription);
        }
    }
}