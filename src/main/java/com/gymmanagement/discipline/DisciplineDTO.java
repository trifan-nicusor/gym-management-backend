package com.gymmanagement.discipline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineDTO {
    private Long id;
    private String name;
    private String shortDescription;
    private String longDescription;
}