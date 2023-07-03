package com.gymmanagement.subscription;

import com.gymmanagement.discipline.DisciplineDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionDTOMapper implements Function<Subscription, SubscriptionDTO> {

    private final DisciplineDTOMapper disciplineMapper;

    @Override
    public SubscriptionDTO apply(Subscription subscription) {
        return new SubscriptionDTO(
                subscription.getId(),
                subscription.getName(),
                subscription.isActive(),
                subscription.getDuration(),
                subscription.getDescription(),
                subscription.getPrice(),
                subscription.getAvailable(),
                subscription.getDisciplineList()
                        .stream()
                        .map(disciplineMapper)
                        .collect(Collectors.toList())
        );
    }
}