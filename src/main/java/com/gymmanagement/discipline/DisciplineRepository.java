package com.gymmanagement.discipline;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    @Query("SELECT d" +
            " FROM Discipline d" +
            " WHERE d.isActive = true")
    List<Discipline> findAllDisciplines();

    @Query("SELECT d" +
            " FROM Discipline d" +
            " WHERE d.name = ?1 AND d.isActive = true")
    Optional<Discipline> findByName(String name);

    @Query("SELECT d" +
            " FROM Discipline d" +
            " WHERE d.isActive = true AND d.id = ?1")
    Optional<Discipline> getDisciplineById(int id);

    @Transactional
    @Modifying
    @Query("UPDATE Discipline d" +
            " SET d.isActive = false" +
            " WHERE d.id = ?1")
    void disableDiscipline(int id);
}