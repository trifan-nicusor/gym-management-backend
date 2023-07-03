package com.gymmanagement.security.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PutMapping
    public void addUserSubscription(@RequestBody SubscriptionRequest request) {
        String email = userService.loadUserByUsername(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()).getUsername();

        userService.addUserSubscription(email, request);
    }
}