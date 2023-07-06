package com.gymmanagement.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c INNER JOIN c.user u" +
            " WHERE u.id = ?1")
    Optional<Cart> getUserCart(Long id);
}