package com.gymmanagement.discipline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    @Query("SELECT d" +
            " FROM Discipline d" +
            " WHERE d.active = true")
    List<Discipline> findAllDisciplines();

    @Query("SELECT d" +
            " FROM Discipline d" +
            " WHERE d.name = ?1")
    Optional<Discipline> findByName(String name);
}