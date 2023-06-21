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

    public EquipmentDTO getEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("Equipment not found exception!")
        );

        if(equipmentRepository.getEquipmentById(id) == null) {
            return null;
        }

        return equipmentMapper.apply(equipment);
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
}