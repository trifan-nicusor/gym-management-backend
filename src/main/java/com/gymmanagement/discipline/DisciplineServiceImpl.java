package com.gymmanagement.discipline;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisciplineServiceImpl implements DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineDTOMapper disciplineMapper;

    @Override
    public List<DisciplineDTO> getAllDisciplines() {
        return disciplineRepository.findAllDisciplines()
                .stream()
                .map(disciplineMapper)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void saveDiscipline(DisciplineRequest request) {
        var discipline = Discipline.builder()
                .name(request.getName())
                .shortDescription(request.getShortDescription())
                .longDescription(request.getLongDescription())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        disciplineRepository.save(discipline);
    }

    @Override
    public DisciplineDTO getDiscipline(int id) {
        Discipline discipline = disciplineRepository.getDisciplineById(id).orElseThrow();

        return disciplineMapper.apply(discipline);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteDiscipline(int id) {
        disciplineRepository.disableDiscipline(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void updateDiscipline(int id, DisciplineRequest request) {
        Discipline discipline = loadDisciplineById(id);

        if (request.getName() != null) {
            discipline.setName(request.getName());
        }

        if (request.getShortDescription() != null) {
            discipline.setShortDescription(request.getShortDescription());
        }

        if (request.getLongDescription() != null) {
            discipline.setLongDescription(request.getLongDescription());
        }

        disciplineRepository.save(discipline);
    }

    @Override
    public Discipline loadDisciplineById(int id) {
        return disciplineRepository.findById((long) id).orElseThrow();
    }

    @Override
    public boolean isDisciplineSaved(String name) {
        return disciplineRepository.findByName(name).isPresent();
    }

    @Override
    public boolean disciplineExists(int id) {
        return disciplineRepository.getDisciplineById(id).isPresent();
    }
}