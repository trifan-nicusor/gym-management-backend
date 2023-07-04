package com.gymmanagement.equipment;

import org.springframework.data.jpa.repository.JpaRepository;
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
            " WHERE e.name = ?1")
    Optional<Equipment> findByName(String name);
}