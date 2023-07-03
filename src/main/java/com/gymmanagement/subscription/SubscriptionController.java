package com.gymmanagement.subscription;

import com.gymmanagement.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    private final SubscriptionServiceImpl subscriptionService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        List<SubscriptionDTO> subscriptions = subscriptionService.getAllSubscriptions();

        if (subscriptions.size() > 0) {
            return new ResponseEntity<>(subscriptions, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> saveSubscription(@RequestBody SubscriptionRequest sRequest) {
        if (!subscriptionService.isSubscriptionSaved(sRequest.getName())) {
            subscriptionService.saveSubscription(sRequest);

            return new ResponseEntity<>("Subscription successfully added!", HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscription(@PathVariable Long id) {

        if (subscriptionService.subscriptionExists(id)) {
            return new ResponseEntity<>(subscriptionService.getSubscription(id), HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubscription(@PathVariable Long id) {

        if (subscriptionService.subscriptionExists(id)) {
            subscriptionService.deleteSubscription(id);
            return new ResponseEntity<>("Subscription successfully deleted!", HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateSubscription(@PathVariable Long id,
                                                     @RequestBody SubscriptionRequest request) {
        if (subscriptionService.subscriptionExists(id)) {
            subscriptionService.updateSubscription(id, request);
            return new ResponseEntity<>("Subscription successfully updated!", HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/me/info")
    public ResponseEntity<List<SubscriptionDTO>> getMySubscriptions() {
        String email = userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getUsername();

        return new ResponseEntity<>(userService.getMySubscriptions(email) ,HttpStatus.OK);
    }
}