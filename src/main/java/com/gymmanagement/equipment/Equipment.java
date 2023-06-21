package com.gymmanagement.equipment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String goals;
    private String positioning;
    private String execution;
    private boolean active;
    private LocalDateTime createdAt;

    public Equipment(String name,
                     String description,
                     String goals,
                     String positioning,
                     String execution,
                     boolean active,
                     LocalDateTime createdAt) {
        this.name = name;
        this.description = description;
        this.goals = goals;
        this.positioning = positioning;
        this.execution = execution;
        this.active = active;
        this.createdAt = createdAt;
    }
}