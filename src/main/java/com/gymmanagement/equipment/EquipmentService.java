package com.gymmanagement.equipment;

import java.util.List;

public interface EquipmentService {
    List<EquipmentDTO> getAllEquipments();

    void saveEquipment(EquipmentRequest request);

    EquipmentDTO getEquipment(Long id);

    void deleteEquipment(Long id);

    Equipment loadEquipmentById(Long id);

    void updateEquipment(Long id, EquipmentRequest request);

    boolean isEquipmentSaved(String name);

    boolean equipmentExists(Long id);
}