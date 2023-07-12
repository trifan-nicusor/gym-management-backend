package com.gymmanagement.cart;

import com.gymmanagement.product.Product;
import com.gymmanagement.product.ProductDTOMapper;
import com.gymmanagement.subscription.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartDTOMapper implements Function<Cart, CartDTO> {

    private final ProductDTOMapper productMapper;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public CartDTO apply(Cart cart) {
        List<Product> products = cart.getProducts();
        List<Long> prices = new ArrayList<>();
        long total;

        if (products != null) {
            products.forEach(product -> {
                long price = subscriptionRepository.findSubscriptionById(product.getSubscriptionId().intValue())
                        .orElseThrow()
                        .getPrice();

                prices.add(price * product.getQuantity());
            });

            total = prices.stream().mapToLong(Long::longValue).sum();

            return new CartDTO(
                    cart.getId(),
                    products.stream()
                            .map(productMapper)
                            .collect(Collectors.toList()),
                    total
            );
        }

        return new CartDTO(
                cart.getId(),
                null,
                0L
        );
    }
}