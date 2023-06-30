package com.gymmanagement.discipline;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DisciplineDTOMapper implements Function<Discipline, DisciplineDTO> {
    @Override
    public DisciplineDTO apply(Discipline discipline) {
        return new DisciplineDTO(
                discipline.getId(),
                discipline.getName(),
                discipline.getShortDescription(),
                discipline.getLongDescription()
        );
    }
}