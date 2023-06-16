package com.gymmanagement.security.auth;

import com.gymmanagement.security.request.EmailRequest;
import com.gymmanagement.security.request.PasswordRequest;
import com.gymmanagement.security.request.RegisterRequest;
import com.gymmanagement.security.request.ResetRequest;
import com.gymmanagement.security.token.confirmation.ConfirmationToken;
import com.gymmanagement.security.token.confirmation.ConfirmationTokenService;
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
    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        boolean isEmailInvalid = userService.userExists(request.getEmail()) || !userService.isEmailValid(request.getEmail());

        if (isEmailInvalid) {
            return new ResponseEntity<>("Email is already taken or has an invalid format!", HttpStatus.BAD_REQUEST);
        }

        authService.register(request);
        return new ResponseEntity<>("User successfully registered!", HttpStatus.CREATED);
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmAccount(@RequestParam("confirmationToken") String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token).orElseThrow();
        User user = confirmationToken.getUser();

        if (userService.userExists(user.getEmail()) && !confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            authService.confirmAccount(user);
            return new ResponseEntity<>("User successfully confirmed!", HttpStatus.OK);
        }

        return new ResponseEntity<>("User not found or the token is expired!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/resend-confirmation-email")
    public void resendConfirmationEmail(@RequestBody EmailRequest request) {
        User user = userService.loadByEmail(request.getEmail()).orElseThrow();

        authService.sendConfirmationEmail(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

        if (userService.userExists(request.getEmail())) {
            return ResponseEntity.ok(authService.authenticate(request));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordRequest request) {
        UserDetails user = userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (userService.checkIfPasswordMatches(user, request.getCurrentPassword()) && !Objects.equals(request.getCurrentPassword(), request.getNewPassword())) {
            userService.changePassword(user, request.getNewPassword());
            return ResponseEntity.ok("Password successfully changed!");
        }

        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/forgot-password")
    public void forgotPassword(@RequestBody EmailRequest request) {
        userService.sendPasswordResetEmail(request.getEmail());
    }

    @PatchMapping("/reset-password")
    public void restPassword(@RequestParam("resetToken") String token,
                             @RequestBody ResetRequest request) {
        userService.resetPassword(token, request.getPassword());
    }
}