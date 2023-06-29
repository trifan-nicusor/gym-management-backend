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
        return equipmentRepository.findAllEquipments().stream().map(equipmentMapper).collect(Collectors.toList());
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
                .active(request.getActive())
                .createdAt(LocalDateTime.now())
                .build();

        equipmentRepository.save(equipment);
    }

    @Override
    public EquipmentDTO getEquipment(Long id) {
        return equipmentMapper.apply(loadEquipmentById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    @Override
    public Equipment loadEquipmentById(Long id) {
        return equipmentRepository.findById(id).orElseThrow();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
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

        if (request.getActive() != null && request.getActive() != equipment.isActive()) {
            equipment.setActive(request.getActive());
        }

        equipmentRepository.save(equipment);
    }

    @Override
    public boolean isEquipmentSaved(String equipmentName) {
        return equipmentRepository.findByName(equipmentName).isPresent();
    }

    @Override
    public boolean equipmentExists(Long id) {
        return equipmentRepository.findById(id).isPresent();
    }
}