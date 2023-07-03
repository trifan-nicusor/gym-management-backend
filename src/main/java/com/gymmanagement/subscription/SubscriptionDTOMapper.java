package com.gymmanagement.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SubscriptionDTOMapper implements Function<Subscription, SubscriptionDTO> {
    @Override
    public SubscriptionDTO apply(Subscription subscription) {
        return new SubscriptionDTO(
                subscription.getId(),
                subscription.getName(),
                subscription.isActive(),
                subscription.getDuration(),
                subscription.getDescription(),
                subscription.getPrice(),
                subscription.getAvailable()
        );
    }
}