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

    private final EquipmentService equipmentService;

    @GetMapping
    public List<EquipmentDTO> getAllEquipments() {
        return equipmentService.getAllEquipments();
    }

    @PostMapping
    public ResponseEntity<String> addEquipment(@RequestBody EquipmentRequest request) {
        equipmentService.addEquipment(request);

        return new ResponseEntity<>("Equipment successfully added!", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public EquipmentDTO getEquipment(@PathVariable Long id) {
        return equipmentService.getEquipment(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEquipment(@PathVariable Long id) {

        if (equipmentService.loadEquipmentById(id) != null) {
            equipmentService.deleteEquipment(id);
            return new ResponseEntity<>("Equipment successfully deleted!", HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateEquipment(@PathVariable Long id,
                                                  @RequestBody EquipmentRequest request) {

        if (equipmentService.loadEquipmentById(id) != null) {
            equipmentService.updateEquipment(id, request);
            return new ResponseEntity<>("Equipment successfully updated!", HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }
}