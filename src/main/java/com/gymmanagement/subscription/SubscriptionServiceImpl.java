package com.gymmanagement.subscription;

import com.gymmanagement.discipline.Discipline;
import com.gymmanagement.discipline.DisciplineService;
import com.gymmanagement.subscription.joinentity.UserSubscriptionRepository;
import com.gymmanagement.security.user.User;
import com.gymmanagement.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final DisciplineService disciplineService;
    private final UserRepository userRepository;
    private final SubscriptionDTOMapper subscriptionMapper;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Override
    public List<SubscriptionDTO> getAllSubscriptions() {
        User user = userRepository.loadByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Subscription> subscriptions = subscriptionRepository.findAllSubscription();
        List<Long> activeSubs = userSubscriptionRepository.getAllCurrentSubscriptions(user.getId());

        activeSubs.forEach(id -> {
            Subscription subscription = loadSubscriptionById(id);

            subscription.setAvailable(Boolean.FALSE);
        });

        return subscriptions.stream()
                .map(subscriptionMapper)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void saveSubscription(SubscriptionRequest request) {
        var subscription = Subscription.builder()
                .name(request.getName())
                .active(request.getActive())
                .duration(request.getDuration())
                .description(request.getDescription())
                .price(request.getPrice())
                .createdAt(LocalDateTime.now())
                .build();

        List<Discipline> disciplineList = new ArrayList<>();

        request.getDisciplineList().forEach(id -> {
            Discipline discipline = disciplineService.loadDisciplineById(id);
            disciplineList.add(discipline);
        });

        subscription.setDisciplineList(disciplineList);

        subscriptionRepository.save(subscription);
    }

    @Override
    public SubscriptionDTO getSubscription(Long id) {
        return subscriptionMapper.apply(loadSubscriptionById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void updateSubscription(Long subId, SubscriptionRequest request) {
        Subscription subscription = loadSubscriptionById(subId);

        if (request.getName() != null) {
            subscription.setName(request.getName());
        }

        if (request.getActive() != null) {
            subscription.setActive(request.getActive());
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

        if (request.getDisciplineList().size() > 0) {
            List<Discipline> disciplineList = new ArrayList<>();

            request.getDisciplineList().forEach(id -> {
                Discipline discipline = disciplineService.loadDisciplineById(id);
                disciplineList.add(discipline);
            });

            subscription.setDisciplineList(disciplineList);
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public Subscription loadSubscriptionById(Long id) {
        return subscriptionRepository.findById(id).orElseThrow();
    }

    @Override
    public boolean isSubscriptionSaved(String name) {
        return subscriptionRepository.findByName(name).isPresent();
    }

    @Override
    public boolean subscriptionExists(Long id) {
        return subscriptionRepository.findById(id).isPresent();
    }
}