package com.gymmanagement.equipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDTO {
    private int id;
    private String name;
    private String description;
    private String goals;
    private String positioning;
    private String execution;
}