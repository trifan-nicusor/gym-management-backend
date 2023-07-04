package com.gymmanagement.subscription;

import com.gymmanagement.discipline.DisciplineDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionDTOMapper implements Function<Subscription, SubscriptionDTO> {

    private final DisciplineDTOMapper disciplineDTOMapper;

    @Override
    public SubscriptionDTO apply(Subscription subscription) {
        return new SubscriptionDTO(
                subscription.getId(),
                subscription.getName(),
                subscription.getIsAvailable(),
                subscription.getDuration(),
                subscription.getDescription(),
                subscription.getPrice(),
                subscription.getIsAvailable(),
                subscription.getDisciplineList()
                        .stream()
                        .map(disciplineDTOMapper)
                        .collect(Collectors.toList())
        );
    }
}