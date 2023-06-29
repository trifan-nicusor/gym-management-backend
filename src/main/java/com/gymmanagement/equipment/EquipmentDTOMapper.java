package com.gymmanagement.equipment;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EquipmentDTOMapper implements Function<Equipment, EquipmentDTO> {
    @Override
    public EquipmentDTO apply(Equipment equipment) {
        return new EquipmentDTO(
                equipment.getId(),
                equipment.getName(),
                equipment.getDescription(),
                equipment.getGoals(),
                equipment.getPositioning(),
                equipment.getExecution(),
                equipment.isActive()
        );
    }
}