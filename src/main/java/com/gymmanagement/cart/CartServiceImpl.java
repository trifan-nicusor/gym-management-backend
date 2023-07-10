package com.gymmanagement.cart;

import com.gymmanagement.security.user.User;
import com.gymmanagement.security.user.UserRepository;
import com.gymmanagement.subscription.Subscription;
import com.gymmanagement.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public void addProduct(CartRequest request) {
        User user = userRepository.loadByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Subscription subscription = subscriptionRepository.findSubscriptionById(request.getSubscriptionId()).orElseThrow();

        if (cartRepository.findUserCart(user.getEmail()).isEmpty()) {
            user.getCarts().add(
                    Cart.builder()
                            .user(user)
                            .build()
            );

            userRepository.save(user);
        }
    }
}