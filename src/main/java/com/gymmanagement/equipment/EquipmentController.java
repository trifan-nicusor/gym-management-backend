package com.gymmanagement.equipment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/equipment")
public class EquipmentController {

    private final EquipmentServiceImpl equipmentService;

    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> getAllEquipments() {
        List<EquipmentDTO> equipments = equipmentService.getAllEquipments();

        if (equipments.size() > 0) {
            return new ResponseEntity<>(equipments, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> saveEquipment(@RequestBody EquipmentRequest request) {
        equipmentService.saveEquipment(request);

        if (equipmentService.isEquipmentSaved(request.getName())) {
            return new ResponseEntity<>("Equipment successfully added!", HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDTO> getEquipment(@PathVariable Long id) {

        if (equipmentService.equipmentExists(id)) {
            return new ResponseEntity<>(equipmentService.getEquipment(id), HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEquipment(@PathVariable Long id) {

        if (equipmentService.equipmentExists(id)) {
            equipmentService.deleteEquipment(id);
            return new ResponseEntity<>("Equipment successfully deleted!", HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateEquipment(@PathVariable Long id,
                                                  @RequestBody EquipmentRequest request) {
        if (equipmentService.equipmentExists(id)) {
            equipmentService.updateEquipment(id, request);
            return new ResponseEntity<>("Equipment successfully updated!", HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }
}