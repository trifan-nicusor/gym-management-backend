package com.gymmanagement.subscription.joinentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionId implements Serializable {
    private Long userId;
    private Long subscriptionId;
}