package com.gymmanagement.cart;

import com.gymmanagement.discipline.Discipline;
import com.gymmanagement.discount.Discount;
import com.gymmanagement.security.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isOrdered;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade =
                    {
                            CascadeType.DETACH,
                            CascadeType.MERGE,
                            CascadeType.REFRESH,
                            CascadeType.PERSIST
                    },
            targetEntity = Cart.class)
    @JoinTable(
            name = "subscription_cart",
            joinColumns = @JoinColumn(name = "subscription_id",
                    nullable = false,
                    updatable = false),
            inverseJoinColumns = @JoinColumn(name = "cart_id",
                    nullable = false,
                    updatable = false)
    )
    private List<Discipline> disciplines;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade =
                    {
                            CascadeType.DETACH,
                            CascadeType.MERGE,
                            CascadeType.REFRESH,
                            CascadeType.PERSIST
                    },
            targetEntity = Cart.class)
    @JoinTable(
            name = "cart_discount",
            joinColumns = @JoinColumn(name = "cart_id",
                    nullable = false,
                    updatable = false),
            inverseJoinColumns = @JoinColumn(name = "discount_id",
                    nullable = false,
                    updatable = false)
    )
    private List<Discount> discounts;
}
