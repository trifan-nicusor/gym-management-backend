package com.gymmanagement.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {
    private int id;
    private String name;
    private boolean isActive;
    private int duration;
    private String description;
    private int price;
    private Boolean isAvailable;
}