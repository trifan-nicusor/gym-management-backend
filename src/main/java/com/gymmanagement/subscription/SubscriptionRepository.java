package com.gymmanagement.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s" +
            " FROM Subscription s" +
            " WHERE s.isActive = true")
    List<Subscription> findAllSubscription();

    @Query("SELECT s" +
            " FROM Subscription s" +
            " WHERE s.name = ?1")
    Optional<Subscription> findByName(String name);
}