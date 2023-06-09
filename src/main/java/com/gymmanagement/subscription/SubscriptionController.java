package com.gymmanagement.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionServiceImpl subscriptionService;

    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        List<SubscriptionDTO> subscriptions = subscriptionService.getAllSubscriptions();

        if (subscriptions.size() > 0) {
            return new ResponseEntity<>(subscriptions, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> saveSubscription(@RequestBody SubscriptionRequest request) {
        if (!subscriptionService.subscriptionExists(request.getName())) {
            subscriptionService.saveSubscription(request);

            return new ResponseEntity<>("Subscription successfully added!", HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscription(@PathVariable int id) {

        if (subscriptionService.subscriptionExists(id)) {
            return new ResponseEntity<>(subscriptionService.getSubscription(id), HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubscription(@PathVariable int id) {

        if (subscriptionService.subscriptionExists(id)) {
            subscriptionService.deleteSubscription(id);
            return new ResponseEntity<>("Subscription successfully deleted!", HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateSubscription(@PathVariable int id,
                                                     @RequestBody SubscriptionRequest request) {
        if (subscriptionService.subscriptionExists(id)) {
            subscriptionService.updateSubscription(id, request);
            return new ResponseEntity<>("Subscription successfully updated!", HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }
}