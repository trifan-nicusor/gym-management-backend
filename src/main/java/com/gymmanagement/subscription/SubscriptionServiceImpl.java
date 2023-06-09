package com.gymmanagement.subscription;

import com.gymmanagement.discipline.Discipline;
import com.gymmanagement.discipline.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final DisciplineRepository disciplineRepository;
    private final SubscriptionDTOMapper subscriptionMapper;

    @Override
    public List<SubscriptionDTO> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAllSubscription();

        subscriptions.forEach(this::removeInactiveDiscipline);

        return subscriptions.stream()
                .map(subscriptionMapper)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void saveSubscription(SubscriptionRequest request) {
        List<Discipline> disciplines = new ArrayList<>();

        if (request.getDisciplineIds().size() > 0) {
            request.getDisciplineIds().forEach(id -> disciplines.add(disciplineRepository.findById(id).orElseThrow()));
        }

        var subscription = Subscription.builder()
                .name(request.getName())
                .isActive(true)
                .duration(request.getDuration())
                .description(request.getDescription())
                .price(request.getPrice())
                .createdAt(LocalDateTime.now())
                .disciplines(disciplines)
                .build();

        subscriptionRepository.save(subscription);
    }

    @Override
    public SubscriptionDTO getSubscription(int id) {
        Subscription subscription = subscriptionRepository.findSubscriptionById(id).orElseThrow();

        return subscriptionMapper.apply(removeInactiveDiscipline(subscription));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteSubscription(int id) {
        subscriptionRepository.disableSubscription(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void updateSubscription(int subId, SubscriptionRequest request) {
        Subscription subscription = loadSubscriptionById(subId);

        if (request.getName() != null) {
            subscription.setName(request.getName());
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

        if (request.getDisciplineIds().size() > 0) {
            subscriptionRepository.deleteIdsFromJoinTable((long) subscription.getId());

            request.getDisciplineIds().forEach(discipline -> {
                Discipline disciplineToSave = disciplineRepository.findById(discipline).orElseThrow();

                subscription.getDisciplines().add(disciplineToSave);
            });

            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public Subscription loadSubscriptionById(int id) {
        return subscriptionRepository.findById((long) id).orElseThrow();
    }

    @Override
    public boolean subscriptionExists(String name) {
        return subscriptionRepository.findByName(name).isPresent();
    }

    @Override
    public boolean subscriptionExists(int id) {
        return subscriptionRepository.findSubscriptionById(id).isPresent();
    }

    private Subscription removeInactiveDiscipline(Subscription subscription) {
        List<Discipline> disciplines = subscription.getDisciplines();
        List<Discipline> disciplinesToRemove = new ArrayList<>();

        disciplines.forEach(discipline -> {
            if (!discipline.getIsActive()) {
                disciplinesToRemove.add(discipline);
            }
        });

        disciplines.removeAll(disciplinesToRemove);
        subscription.setDisciplines(disciplines);

        return subscription;
    }
}