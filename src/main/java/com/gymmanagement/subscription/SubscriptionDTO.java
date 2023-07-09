package com.gymmanagement.subscription;

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
    private int id;
    private String name;
    private int duration;
    private String description;
    private int price;
    private List<String> disciplineNameList;
}