package com.gymmanagement.security.auth;

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
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        boolean isEmailInvalid = authService.userExists(request.getEmail()) || !authService.isEmailValid(request.getEmail());

        if (isEmailInvalid) {
            return new ResponseEntity<>("Email is already taken or has an invalid format!", HttpStatus.BAD_REQUEST);
        }

        authService.register(request);
        return new ResponseEntity<>("User successfully registered!", HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/confirm-account")
    public void confirmAccount(@RequestParam("email") String email) {
        authService.confirmAccount(email);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam("newPassword") String newPassword,
            @RequestParam("currentPassword") String currentPassword) {
        UserDetails user = userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (userService.checkIfPasswordMatches(user, currentPassword) && !Objects.equals(currentPassword, newPassword)) {
            userService.changePassword(user, newPassword);
            return ResponseEntity.ok("Password successfully changed!");
        }

        System.out.println("ceva");
        return ResponseEntity.badRequest().build();
    }
}