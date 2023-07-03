package com.gymmanagement.subscription;

import com.gymmanagement.discipline.DisciplineDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {

    private Long id;
    private String name;
    private boolean active;
    private int duration;
    private String description;
    private int price;
    private Boolean available;
    private List<DisciplineDTO> disciplineList;
}