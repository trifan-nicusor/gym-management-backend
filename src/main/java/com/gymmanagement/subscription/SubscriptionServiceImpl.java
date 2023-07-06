package com.gymmanagement.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionDTOMapper subscriptionMapper;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionRepository.findAllSubscription()
                .stream()
                .map(subscriptionMapper)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void saveSubscription(SubscriptionRequest request) {
        var subscription = Subscription.builder()
                .name(request.getName())
                .isActive(request.getIsActive())
                .duration(request.getDuration())
                .description(request.getDescription())
                .price(request.getPrice())
                .createdAt(LocalDateTime.now())
                .build();

        subscriptionRepository.save(subscription);
    }

    @Override
    public SubscriptionDTO getSubscription(int id) {
        return subscriptionMapper.apply(loadSubscriptionById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteSubscription(int id) {
        subscriptionRepository.deleteById((long) id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void updateSubscription(int subId, SubscriptionRequest request) {
        Subscription subscription = loadSubscriptionById(subId);

        if (request.getName() != null) {
            subscription.setName(request.getName());
        }

        if (request.getIsActive() != null) {
            subscription.setActive(request.getIsActive());
        }

        if (request.getDuration() != null) {
            subscription.setDuration(request.getDuration());
        }

        if (request.getDescription() != null) {
            subscription.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            subscription.setPrice(request.getPrice());
        }
    }

    @Override
    public Subscription loadSubscriptionById(int id) {
        return subscriptionRepository.findById((long) id).orElseThrow();
    }

    @Override
    public boolean isSubscriptionSaved(String name) {
        return subscriptionRepository.findByName(name).isPresent();
    }

    @Override
    public boolean subscriptionExists(int id) {
        return subscriptionRepository.findById((long) id).isPresent();
    }
}