package com.gymmanagement.equipment;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("SELECT e" +
            " FROM Equipment e" +
            " WHERE e.isActive = true")
    List<Equipment> findAllEquipments();

    @Query("SELECT e" +
            " FROM Equipment e" +
            " WHERE e.name = ?1 AND e.isActive = true")
    Optional<Equipment> findByName(String name);

    @Query("SELECT e" +
            " FROM Equipment e" +
            " WHERE e.id = ?1 AND e.isActive = true")
    Optional<Equipment> getEquipmentById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Equipment e" +
            " SET e.isActive = false" +
            " WHERE e.id = ?1")
    void disableEquipment(long id);
}