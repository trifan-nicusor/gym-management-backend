package com.gymmanagement.security.auth;

import com.gymmanagement.security.request.EmailRequest;
import com.gymmanagement.security.request.PasswordRequest;
import com.gymmanagement.security.request.RegisterRequest;
import com.gymmanagement.security.request.ResetRequest;
import com.gymmanagement.security.token.confirmation.ConfirmationTokenServiceImpl;
import com.gymmanagement.security.token.reset.ResetToken;
import com.gymmanagement.security.token.reset.ResetTokenServiceImpl;
import com.gymmanagement.security.user.User;
import com.gymmanagement.security.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authService;
    private final ConfirmationTokenServiceImpl confirmationTokenService;
    private final ResetTokenServiceImpl resetTokenService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest request) {

        if (authService.isEmailValid(request.getEmail())) {
            authService.signup(request);
            return ResponseEntity.ok("User successfully registered!");
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmAccount(@RequestParam("confirmationToken") String token) {

        if (confirmationTokenService.isLinkValid(token)) {
            authService.confirmAccount(token);
            return ResponseEntity.ok("Account successfully confirmed!");
        }

        return new ResponseEntity<>("Expired link!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/resend-confirmation-email")
    public ResponseEntity<String> resendConfirmationEmail(@RequestBody EmailRequest request) {
        String email = request.getEmail();

        if (userService.userExists(email)) {
            User user = userService.loadByEmail(email);

            if (user.getConfirmedAt() == null && confirmationTokenService.hasTokenAvailable(user.getId())) {
                authService.sendConfirmationEmail(user);
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        String email = request.getEmail();

        if (userService.userExists(email) && userService.isUserEnabled(email)) {
            return ResponseEntity.ok(authService.login(request));
        }

        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordRequest request) {
        UserDetails user = userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        String currentPassword = request.getCurrentPassword();
        String newPassword = request.getNewPassword();

        if (userService.checkIfPasswordMatches(user, currentPassword) && !Objects.equals(currentPassword, newPassword)) {
            userService.changePassword(user, newPassword);
            return ResponseEntity.ok("Password successfully changed!");
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest request) {
        String email = request.getEmail();

        if (userService.userExists(email) && userService.isUserEnabled(email)) {
            User user = userService.loadByEmail(email);

            if (!resetTokenService.hasTokenAvailable(user.getId())) {
                userService.sendPasswordResetEmail(email);
                return ResponseEntity.ok("Email successfully sent!");
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<String> restPassword(@RequestParam("resetToken") String token,
                                               @RequestBody ResetRequest request) {
        ResetToken resetToken;

        if (resetTokenService.isTokenPresent(token)) {
            resetToken = resetTokenService.findByToken(token).orElseThrow();

            if (!resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                userService.resetPassword(resetToken, request.getPassword());
                return ResponseEntity.ok("Password successfully reset!");
            }
        }

        return ResponseEntity.badRequest().build();
    }
}