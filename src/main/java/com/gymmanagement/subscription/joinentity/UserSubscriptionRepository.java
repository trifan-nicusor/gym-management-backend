package com.gymmanagement.subscription.joinentity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    @Query("SELECT us FROM user_subscription us WHERE us.userId = ?1 and us.subscriptionId = ?2")
    UserSubscription findById(Long userId, Long subscriptionId);

    @Query("SELECT us.subscriptionId FROM user_subscription us WHERE us.userId = ?1")
    List<Long> getAllCurrentSubscriptions(Long userId);
}