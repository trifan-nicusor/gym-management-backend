package com.gymmanagement.subscription;

import java.util.List;

public interface SubscriptionService {
    List<SubscriptionDTO> getAllSubscriptions();

    void saveSubscription(SubscriptionRequest request);

    SubscriptionDTO getSubscription(Long id);

    void deleteSubscription(Long id);

    void updateSubscription(Long id, SubscriptionRequest request);

    Subscription loadSubscriptionById(Long id);

    boolean isSubscriptionSaved(String name);

    boolean subscriptionExists(Long id);
}