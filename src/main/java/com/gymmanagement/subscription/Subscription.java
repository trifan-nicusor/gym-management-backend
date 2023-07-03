package com.gymmanagement.subscription;

import com.gymmanagement.discipline.Discipline;
import com.gymmanagement.security.user.User;
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
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private boolean active;
    private int duration;
    private String description;
    private int price;
    private Boolean available;
    private LocalDateTime createdAt;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade =
                    {
                            CascadeType.DETACH,
                            CascadeType.MERGE,
                            CascadeType.REFRESH,
                            CascadeType.PERSIST
                    },
            targetEntity = Discipline.class)
    @JoinTable(
            name = "subscription_discipline",
            inverseJoinColumns = @JoinColumn(name = "discipline_id",
                    nullable = false,
                    updatable = false),
            joinColumns = @JoinColumn(name = "subscription_id",
                    nullable = false,
                    updatable = false)
    )
    private List<Discipline> disciplineList;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade =
                    {
                            CascadeType.DETACH,
                            CascadeType.MERGE,
                            CascadeType.REFRESH,
                            CascadeType.PERSIST
                    },
            targetEntity = User.class)
    @JoinTable(
            name = "user_subscription",
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    nullable = false,
                    updatable = false),
            joinColumns = @JoinColumn(name = "subscription_id",
                    nullable = false,
                    updatable = false)
    )
    private List<User> userList;
}