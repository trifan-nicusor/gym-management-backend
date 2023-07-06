package com.gymmanagement.cart;

import com.gymmanagement.security.user.User;
import com.gymmanagement.security.user.UserRepository;
import com.gymmanagement.subscription.SubscriptionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final SubscriptionServiceImpl subscriptionService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public void addProduct(AddSubscriptionRequest request) {
        User user = userRepository.loadByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        var cart = new Cart();

        if (cartRepository.getUserCart(user.getId()).isEmpty()) {
            cart = Cart.builder()
                    .subscriptionList(Collections.singletonList(subscriptionService.loadSubscriptionById(request.getSubscriptionId())))
                    .user(user)
                    .ordered(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            cartRepository.save(cart);
        }
    }
}