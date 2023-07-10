package com.gymmanagement.discount;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String code;
    private String type;
    private Long usePerUser;
    private Long quantity;
    private Long minimumCartTotal;
    private boolean compatibleWithOther;
    private Date validFrom;
    private Date validTo;
    private Boolean isActive;
    private LocalDateTime createdAt;
}