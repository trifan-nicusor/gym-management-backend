package com.gymmanagement.product;

import com.gymmanagement.subscription.SubscriptionDTO;
import com.gymmanagement.subscription.SubscriptionDTOMapper;
import com.gymmanagement.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductDTOMapper implements Function<Product, ProductDTO> {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionDTOMapper subscriptionMapper;

    @Override
    public ProductDTO apply(Product product) {
        SubscriptionDTO subscription = subscriptionMapper.apply(subscriptionRepository.findSubscriptionById(product.getSubscriptionId().intValue()).orElseThrow());

        return new ProductDTO(
                product.getId(),
                subscription,
                product.getQuantity()
        );
    }
}