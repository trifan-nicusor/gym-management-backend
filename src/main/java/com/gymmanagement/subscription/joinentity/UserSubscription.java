package com.gymmanagement.subscription.joinentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_subscription")
@IdClass(UserSubscriptionId.class)
public class UserSubscription {

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Id
    @Column(name = "subscription_id")
    private Long subscriptionId;
    private LocalDateTime expiresAt;
}