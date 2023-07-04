package com.gymmanagement.discipline;

import com.gymmanagement.subscription.Subscription;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private String shortDescription;
    private String longDescription;
    private boolean isActive;
    private LocalDateTime createdAt;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade =
                    {
                            CascadeType.DETACH,
                            CascadeType.MERGE,
                            CascadeType.REFRESH,
                            CascadeType.PERSIST
                    },
            targetEntity = Subscription.class)
    @JoinTable(
            name = "subscription_discipline",
            joinColumns = @JoinColumn(name = "subscription_id",
                    nullable = false,
                    updatable = false),
            inverseJoinColumns = @JoinColumn(name = "discipline_id",
                    nullable = false,
                    updatable = false)
    )
    private List<Subscription> subscriptionList;
}