package com.gymmanagement.discipline;

import java.util.List;

public interface DisciplineService {
    List<DisciplineDTO> getAllDisciplines();

    void saveDiscipline(DisciplineRequest request);

    DisciplineDTO getDiscipline(int id);

    void deleteDiscipline(int id);

    void updateDiscipline(int id, DisciplineRequest request);

    Discipline loadDisciplineById(int id);

    boolean isDisciplineSaved(String name);

    boolean disciplineExists(int id);
}