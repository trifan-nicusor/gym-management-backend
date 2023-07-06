package com.gymmanagement.subscription;

import java.util.List;

public interface SubscriptionService {
    List<SubscriptionDTO> getAllSubscriptions();

    void saveSubscription(SubscriptionRequest request);

    SubscriptionDTO getSubscription(int id);

    void deleteSubscription(int id);

    void updateSubscription(int id, SubscriptionRequest request);

    Subscription loadSubscriptionById(int id);

    boolean isSubscriptionSaved(String name);

    boolean subscriptionExists(int id);
}