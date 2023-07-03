package com.gymmanagement.discipline;

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
@RequestMapping("/api/v1/discipline")
public class DisciplineController {

    private final DisciplineServiceImpl disciplineService;

    @GetMapping
    public ResponseEntity<List<DisciplineDTO>> getAllDisciplines() {
        List<DisciplineDTO> disciplines = disciplineService.getAllDisciplines();

        if (disciplines.size() > 0) {
            return new ResponseEntity<>(disciplines, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> saveDiscipline(@RequestBody DisciplineRequest request) {
        disciplineService.saveDiscipline(request);

        if (disciplineService.isDisciplineSaved(request.getName())) {
            return new ResponseEntity<>("Discipline successfully added!", HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineDTO> getDiscipline(@PathVariable Long id) {

        if (disciplineService.disciplineExists(id)) {
            return new ResponseEntity<>(disciplineService.getDiscipline(id), HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiscipline(@PathVariable Long id) {

        if (disciplineService.disciplineExists(id)) {
            disciplineService.deleteDiscipline(id);
            return new ResponseEntity<>("Discipline successfully deleted!", HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateEquipment(@PathVariable Long id,
                                                  @RequestBody DisciplineRequest request) {
        if (disciplineService.disciplineExists(id)) {
            disciplineService.updateDiscipline(id, request);
            return new ResponseEntity<>("Equipment successfully updated!", HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }
}