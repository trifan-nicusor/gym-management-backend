package com.gymmanagement.equipment;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EquipmentServiceTest {

    @MockBean
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentServiceImpl equipmentService;

    private Equipment equipment;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setupTwoEquipments() {
        equipment = Equipment.builder()
                .id(1L)
                .name("name1")
                .description("description1")
                .goals("goals1")
                .positioning("positioning1")
                .execution("execution1")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllEquipments() {
        Equipment secondEquipment = Equipment.builder()
                .id(2L)
                .name("name2")
                .description("description2")
                .goals("goals2")
                .positioning("positioning2")
                .execution("execution2")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        given(equipmentRepository.findAllEquipments()).willReturn(List.of(equipment, secondEquipment));

        List<EquipmentDTO> equipments = equipmentService.getAllEquipments();

        assertNotNull(equipments);
        assertEquals(2, equipments.size());
    }

    @WithMockUser(authorities = "ADMIN")
    @Test
    void saveEquipment() {
        /*given(equipmentRepository.findByName(equipment.getName())).willReturn(Optional.empty());

        given(equipmentRepository.save(equipment)).willReturn(equipment);

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when -  action or the behaviour that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        System.out.println(savedEmployee);
        // then - verify the output
        assertThat(savedEmployee).isNotNull();*/
    }

    @Test
    void getEquipment() {
    }

    @Test
    void deleteEquipment() {
    }

    @Test
    void loadEquipmentById() {
    }

    @Test
    void updateEquipment() {
    }

    @Test
    void isEquipmentSaved() {
    }
}