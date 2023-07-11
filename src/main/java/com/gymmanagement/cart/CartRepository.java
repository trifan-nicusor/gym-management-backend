package com.gymmanagement.cart;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c" +
            " FROM Cart c" +
            " INNER JOIN User u" +
            " WHERE u.id = ?1 AND c.isOrdered = false")
    Optional<Cart> findCartByUser(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product p" +
            " SET p.quantity = quantity + 1" +
            " WHERE p.cart_id = ?1 AND p.subscription_id = ?2", nativeQuery = true)
    void increaseQuantity(Long id, Long subscriptionId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM product p" +
            " WHERE p.id = ?1", nativeQuery = true)
    void delete(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product p" +
            " SET p.quantity = ?2" +
            " WHERE p.id = ?1", nativeQuery = true)
    void updateQuantity(Long id, Long quantity);
}