package com.gymmanagement.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT d" +
            " FROM Discount d" +
            " WHERE d.isActive = true")
    List<Discount> findAllDiscounts();

    @Query("SELECT d" +
            " FROM Discount d" +
            " WHERE d.name = ?1 AND d.isActive = true")
    Optional<Discount> getDiscountByName(String name);

    @Query("SELECT d" +
            " FROM Discount d" +
            " WHERE d.id = ?1 AND d.isActive = true")
    Optional<Discount> getDiscountById(Long id);
}