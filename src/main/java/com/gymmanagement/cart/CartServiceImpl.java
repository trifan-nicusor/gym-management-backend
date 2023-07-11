package com.gymmanagement.cart;

import com.gymmanagement.cart.request.AddProductRequest;
import com.gymmanagement.cart.request.UpdateProductRequest;
import com.gymmanagement.security.user.User;
import com.gymmanagement.security.user.UserRepository;
import com.gymmanagement.security.user.UserService;
import com.gymmanagement.subscription.Subscription;
import com.gymmanagement.subscription.SubscriptionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final SubscriptionServiceImpl subscriptionService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CartDTOMapper cartMapper;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public CartDTO getCart() {
        User user = userService.loadByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Cart cart = user.getCart();

        return cartMapper.apply(cart);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public void addProduct(AddProductRequest request) {
        User user = userRepository.loadByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Subscription subscription = subscriptionService.loadSubscriptionById(request.getSubscriptionId().intValue());

        if (cartRepository.findCartByUser(user.getId()).isEmpty()) {
            user.setCart(Cart.builder()
                    .user(user)
                    .isOrdered(false)
                    .build());
        }

        var cart = user.getCart();

        if (cart.getSubscriptions() != null && cart.getSubscriptions().contains(subscription)) {
            cartRepository.increaseQuantity(cart.getId(), (long) subscription.getId());
        }

        if (cart.getSubscriptions() == null) {
            cart.setSubscriptions(Collections.singletonList(subscription));
        } else {
            List<Subscription> subscriptions = cart.getSubscriptions();
            subscriptions.add(subscription);

            cart.setSubscriptions(subscriptions);
        }

        user.setCart(cart);
        userRepository.save(user);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public void deleteProduct(Long id) {
        cartRepository.delete(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public void updateProduct(Long id, UpdateProductRequest request) {
        cartRepository.updateQuantity(id, request.getQuantity());
    }
}