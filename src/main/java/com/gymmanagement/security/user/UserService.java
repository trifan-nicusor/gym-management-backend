package com.gymmanagement.security.user;

import com.gymmanagement.security.email.EmailSender;
import com.gymmanagement.security.email.EmailValidator;
import com.gymmanagement.security.email.builder.EmailBuilderService;
import com.gymmanagement.security.token.reset.ResetToken;
import com.gymmanagement.security.token.reset.ResetTokenRepository;
import com.gymmanagement.security.token.reset.ResetTokenServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final EmailSender emailSender;
    private final EmailBuilderService emailBuilderService;
    private final ResetTokenServiceImpl resetTokenService;
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void resetPassword(ResetToken token, String password) {
        User user = token.getUser();
        resetTokenService.getUserLastToken(user.getId()).setExpiresAt(LocalDateTime.now());

        changePassword(user, password);
    }

    public boolean isUserEnabled(String email) {
        return userRepository.isUserEnabled(email);
    }
}