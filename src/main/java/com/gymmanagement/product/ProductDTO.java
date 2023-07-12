package com.gymmanagement.product;

import com.gymmanagement.subscription.SubscriptionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private SubscriptionDTO subscription;
    private Long quantity;
}