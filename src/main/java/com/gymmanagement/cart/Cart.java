package com.gymmanagement.cart;

import com.gymmanagement.discount.Discount;
import com.gymmanagement.product.Product;
import com.gymmanagement.security.user.User;
import com.gymmanagement.subscription.Subscription;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
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
            name = "product",
            joinColumns = @JoinColumn(name = "cart_id",
                    nullable = false,
                    updatable = false),
            inverseJoinColumns = @JoinColumn(name = "subscription_id",
                    nullable = false,
                    updatable = false)
    )
    private List<Subscription> subscriptions;
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
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL
    )
    private List<Product> products;
}
