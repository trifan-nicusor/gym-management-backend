package com.gymmanagement.security.user;

import com.gymmanagement.security.email.EmailSender;
import com.gymmanagement.security.email.EmailValidator;
import com.gymmanagement.security.emailbuilder.EmailBuilderService;
import com.gymmanagement.security.resettoken.ResetToken;
import com.gymmanagement.security.resettoken.ResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public void changePassword(UserDetails user, String password) {
        userRepository.updatePassword(user.getUsername(), passwordEncoder.encode(password));
    }

    public boolean checkIfPasswordMatches(UserDetails user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isEmailValid(String email) {
        return emailValidator.test(email);
    }

    public void enableUser(String email) {
        userRepository.enableUser(email);
    }

    public void sendPasswordResetEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        String token = UUID.randomUUID().toString();
        String link = "http://localhost:8080/api/v1/auth/reset-password?resetToken=" + token;

        var resetToken = ResetToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        resetTokenRepository.save(resetToken);

        emailSender.send(email, emailBuilderService.forgotPasswordEmailBuilder(user.getFirstName(), link));
    }

    public void resetPassword(String token, String password) {
        ResetToken resetToken = resetTokenRepository.findByToken(token).orElseThrow();
        UserDetails user = resetToken.getUser();

        changePassword(user, password);
    }
}