package com.gymmanagement.discipline;

import java.util.List;

public interface DisciplineService {
    List<DisciplineDTO> getAllDisciplines();

    void saveDiscipline(DisciplineRequest request);

    DisciplineDTO getDiscipline(Long id);

    void deleteDiscipline(Long id);

    void updateDiscipline(Long id, DisciplineRequest request);

    Discipline loadDisciplineById(Long id);

    boolean isDisciplineSaved(String name);

    boolean disciplineExists(Long id);
}