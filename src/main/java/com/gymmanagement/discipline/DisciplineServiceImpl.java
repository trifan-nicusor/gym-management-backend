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
                .active(request.getActive())
                .createdAt(LocalDateTime.now())
                .build();

        disciplineRepository.save(discipline);
    }

    @Override
    public DisciplineDTO getDiscipline(Long id) {
        return disciplineMapper.apply(loadDisciplineById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteDiscipline(Long id) {
        disciplineRepository.deleteById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void updateDiscipline(Long id, DisciplineRequest request) {
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

        if (request.getActive() != null && request.getActive() != discipline.isActive()) {
            discipline.setActive(request.getActive());
        }

        disciplineRepository.save(discipline);
    }

    @Override
    public Discipline loadDisciplineById(Long id) {
        return disciplineRepository.findById(id).orElseThrow();
    }

    @Override
    public boolean isDisciplineSaved(String name) {
        return disciplineRepository.findByName(name).isPresent();
    }

    @Override
    public boolean disciplineExists(Long id) {
        return disciplineRepository.findById(id).isPresent();
    }
}