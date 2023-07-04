package com.gymmanagement.subscription;

import com.gymmanagement.discipline.Discipline;
import com.gymmanagement.discipline.DisciplineService;
import com.gymmanagement.security.user.User;
import com.gymmanagement.security.user.UserRepository;
import com.gymmanagement.subscription.joinentity.UserSubscriptionRepository;
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
            Subscription subscription = loadSubscriptionById(id.intValue());

            subscription.setIsAvailable(Boolean.FALSE);
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
                .isActive(request.getIsActive())
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<SubscriptionDTO> getMySubscriptions() {
        String email = userRepository.loadByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getUsername();
        List<Long> subscriptionIds = userSubscriptionRepository.getAllCurrentSubscriptions(userRepository.loadByEmail(email).getId());
        List<Subscription> mySubscriptions = new ArrayList<>();

        subscriptionIds.forEach(id -> mySubscriptions.add(subscriptionRepository.findById(id).orElseThrow()));

        return mySubscriptions.stream()
                .map(subscriptionMapper)
                .collect(Collectors.toList());
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