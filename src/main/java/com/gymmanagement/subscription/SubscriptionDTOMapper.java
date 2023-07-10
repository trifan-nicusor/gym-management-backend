package com.gymmanagement.subscription;

import com.gymmanagement.discipline.Discipline;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionDTOMapper implements Function<Subscription, SubscriptionDTO> {
    @Override
    public SubscriptionDTO apply(Subscription subscription) {
        return new SubscriptionDTO(
                subscription.getId(),
                subscription.getName(),
                subscription.getDuration(),
                subscription.getDescription(),
                subscription.getPrice(),
                subscription.getDisciplines()
                        .stream()
                        .map(Discipline::getName)
                        .collect(Collectors.toList())
        );
    }
}