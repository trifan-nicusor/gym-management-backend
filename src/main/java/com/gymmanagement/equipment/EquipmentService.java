package com.gymmanagement.equipment;

import java.util.List;

public interface EquipmentService {
    List<EquipmentDTO> getAllEquipments();

    void saveEquipment(EquipmentRequest request);

    EquipmentDTO getEquipment(int id);

    void deleteEquipment(int id);

    void updateEquipment(int id, EquipmentRequest request);

    Equipment loadEquipmentById(int id);

    boolean isEquipmentSaved(String name);

    boolean equipmentExists(int id);
}