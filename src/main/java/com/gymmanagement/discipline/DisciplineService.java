package com.gymmanagement.discipline;

import java.util.List;

public interface DisciplineService {
    List<DisciplineDTO> getAllDisciplines();

    void saveDiscipline(DisciplineRequest request);

    DisciplineDTO getDiscipline(int id);

    void deleteDiscipline(int id);

    void updateDiscipline(int id, DisciplineRequest request);

    boolean isDisciplineSaved(String name);

    Discipline loadDisciplineById(int id);

    boolean disciplineExists(int id);
}