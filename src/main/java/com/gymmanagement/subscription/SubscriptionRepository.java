package com.gymmanagement.subscription;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
            " WHERE s.name = ?1 AND s.isActive = true")
    Optional<Subscription> findByName(String name);

    @Query("SELECT s" +
            " FROM Subscription s" +
            " WHERE s.id = ?1 AND s.isActive = true ")
    Optional<Subscription> findSubscriptionById(int id);

    @Transactional
    @Modifying
    @Query("UPDATE Subscription s" +
            " SET s.isActive = false" +
            " WHERE s.id = ?1")
    void disableSubscription(long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM `subscription_discipline`" +
            " WHERE subscription_id = ?1",
            nativeQuery = true)
    void deleteIdsFromJoinTable(Long id);
}