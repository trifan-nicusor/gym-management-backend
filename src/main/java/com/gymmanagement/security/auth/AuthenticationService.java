package com.gymmanagement.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymmanagement.security.config.JwtService;
import com.gymmanagement.security.email.EmailSender;
import com.gymmanagement.security.email.builder.EmailBuilderService;
import com.gymmanagement.security.request.RegisterRequest;
import com.gymmanagement.security.token.confirmation.ConfirmationToken;
import com.gymmanagement.security.token.confirmation.ConfirmationTokenRepository;
import com.gymmanagement.security.token.confirmation.ConfirmationTokenService;
import com.gymmanagement.security.token.jwt.JwtToken;
import com.gymmanagement.security.token.jwt.JwtTokenRepository;
import com.gymmanagement.security.user.User;
import com.gymmanagement.security.user.UserRepository;
import com.gymmanagement.security.user.UserRole;
import com.gymmanagement.security.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRepository jwtTokenRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailBuilderService emailBuilderService;
    private final EmailSender emailSender;
    private final UserService userService;
    @Value("${domain}")
    private String domain;
    @Value("${uuid.token.expiration}")
    private int expireTime;

    public void signup(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .updatedAt(LocalDateTime.now())
                .isEnabled(false)
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        saveUserToken(savedUser, jwtToken);
        sendConfirmationEmail(user);
    }

    public void confirmAccount(String token) {
        User user = confirmationTokenRepository.loadByToken(token).getUser();
        confirmationTokenService.getLastUserToken(user.getId()).setExpiresAt(LocalDateTime.now());

        userService.enableUser(user);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);

                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public void sendConfirmationEmail(User user) {
        String token = UUID.randomUUID().toString();
        String link = domain + "/api/v1/auth/confirm-account?confirmationToken=" + token;

        if (user.getConfirmedAt() == null) {
            var buildToken = ConfirmationToken.builder()
                    .token(token)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusMinutes(expireTime))
                    .user(user)
                    .build();

            confirmationTokenRepository.save(buildToken);
            emailSender.send(user.getEmail(), emailBuilderService.confirmationEmailBuilder(user.getFirstName(), link));
        }
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = jwtTokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(jwtToken -> {
            jwtToken.setExpired(true);
            jwtToken.setRevoked(true);
        });

        jwtTokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = JwtToken.builder()
                .user(user)
                .token(jwtToken)
                .isExpired(false)
                .isRevoked(false)
                .build();

        jwtTokenRepository.save(token);
    }

    public boolean isEmailValid(String email) {
        return !userService.userExists(email) && userService.isEmailValid(email);
    }
}