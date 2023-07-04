package com.gymmanagement.equipment;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentDTOMapper equipmentMapper;

    @Override
    public List<EquipmentDTO> getAllEquipments() {
        return equipmentRepository.findAllEquipments()
                .stream()
                .map(equipmentMapper)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void saveEquipment(EquipmentRequest request) {
        var equipment = Equipment.builder()
                .name(request.getName())
                .description(request.getDescription())
                .goals(request.getGoals())
                .positioning(request.getPositioning())
                .execution(request.getExecution())
                .isActive(request.getIsActive())
                .createdAt(LocalDateTime.now())
                .build();

        equipmentRepository.save(equipment);
    }

    @Override
    public EquipmentDTO getEquipment(int id) {
        return equipmentMapper.apply(loadEquipmentById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteEquipment(int id) {
        equipmentRepository.deleteById((long) id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void updateEquipment(int id, EquipmentRequest request) {
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

        if (request.getIsActive() != null && request.getIsActive() != equipment.isActive()) {
            equipment.setActive(request.getIsActive());
        }

        equipmentRepository.save(equipment);
    }

    @Override
    public Equipment loadEquipmentById(int id) {
        return equipmentRepository.findById((long) id).orElseThrow();
    }

    @Override
    public boolean isEquipmentSaved(String name) {
        return equipmentRepository.findByName(name).isPresent();
    }

    @Override
    public boolean equipmentExists(int id) {
        return equipmentRepository.findById((long) id).isPresent();
    }
}