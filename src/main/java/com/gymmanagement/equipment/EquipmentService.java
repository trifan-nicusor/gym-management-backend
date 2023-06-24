package com.gymmanagement.equipment;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentDTOMapper equipmentMapper;

    public List<EquipmentDTO> getAllEquipments() {
        return equipmentRepository.findAll()
                .stream()
                .map(equipmentMapper)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void addEquipment(EquipmentRequest request) {
        var equipment = Equipment.builder()
                .name(request.getName())
                .description(request.getDescription())
                .goals(request.getGoals())
                .positioning(request.getPositioning())
                .execution(request.getExecution())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        equipmentRepository.save(equipment);
    }

    public EquipmentDTO getEquipment(Long id) {
        Equipment equipment = loadEquipmentById(id);
        return equipmentMapper.apply(equipment);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    public Equipment loadEquipmentById(Long id) {
        return equipmentRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("Equipment not found exception!")
        );
    }

    public void updateEquipment(Long id, EquipmentRequest request) {
        Equipment equipment = loadEquipmentById(id);

        if (request.getName() != null) {
            equipment.setName(request.getName());
        }

        if (request.getDescription() != null) {
            equipment.setDescription(request.getDescription());
        }

        if (request.getGoals() != null) {
            equipment.setGoals(request.getGoals());
        }

        if (request.getPositioning() != null) {
            equipment.setPositioning(request.getPositioning());
        }

        if (request.getExecution() != null) {
            equipment.setExecution(request.getExecution());
        }

        if (request.getIsActive() != null) {
            equipment.setActive(request.getIsActive());
        }

        equipmentRepository.save(equipment);
    }
}