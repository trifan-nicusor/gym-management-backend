package com.gymmanagement.equipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SaveRequest {
    private String name;
    private String description;
    private String goals;
    private String positioning;
    private String execution;
}