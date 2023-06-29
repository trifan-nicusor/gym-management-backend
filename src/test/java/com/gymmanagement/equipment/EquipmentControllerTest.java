package com.gymmanagement.equipment;

import com.gymmanagement.AbstractTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentControllerTest extends AbstractTest {

    private final static String baseUri = "/api/v1/equipment";

    @Override
    @BeforeAll
    public void setUp() {
        super.setUp();
    }

    @Test
    void getEquipmentList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        EquipmentDTO[] equipments = super.mapFromJson(content, EquipmentDTO[].class);
        assertTrue(equipments.length > 0);
    }

    @WithMockUser(authorities = "ADMIN")
    @Test
    void saveEquipment() throws Exception {
        var equipment = EquipmentRequest.builder()
                .name("name")
                .description("description")
                .goals("goals")
                .positioning("positioning")
                .execution("execution")
                .build();

        String inputJson = super.mapToJson(equipment);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(baseUri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(201, status);

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(content, "Equipment successfully added!");
    }

    @WithMockUser(authorities = {"ADMIN", "USER"})
    @Test
    void getEquipment() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "/1")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        EquipmentDTO equipment = super.mapFromJson(content, EquipmentDTO.class);

        assertNotNull(equipment);
    }

    @WithMockUser(authorities = "ADMIN")
    @Test
    void deleteProduct() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(baseUri + "/1"))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();

        assertEquals(content, "Equipment successfully deleted!");
    }

    @WithMockUser(authorities = "ADMIN")
    @Test
    void updateProduct() throws Exception {
        var equipment = EquipmentRequest.builder()
                .active(false)
                .build();

        String inputJson = super.mapToJson(equipment);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(baseUri + "/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Equipment successfully updated!");
    }
}