package com.gymmanagement.discount;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class DiscountDTOMapper implements Function<Discount, DiscountDTO> {
    @Override
    public DiscountDTO apply(Discount discount) {
        return new DiscountDTO(
                discount.getId(),
                discount.getName(),
                discount.getCode(),
                discount.getType(),
                discount.getUsePerUser(),
                discount.getQuantity(),
                discount.getMinimumCartTotal(),
                discount.isCompatibleWithOther(),
                discount.getValidFrom(),
                discount.getValidTo()
        );
    }
}
