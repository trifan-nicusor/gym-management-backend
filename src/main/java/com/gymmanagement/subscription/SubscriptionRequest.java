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
public class SubscriptionRequest {
    private String name;
    private Boolean active;
    private Integer duration;
    private String description;
    private Integer price;
    List<Long> disciplineList;
}