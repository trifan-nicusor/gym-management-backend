package com.gymmanagement.discount;

import java.util.List;

public interface DiscountService {
    List<DiscountDTO> getAllDiscounts();

    boolean discountExists(String name);

    void updateDiscount(int subId, DiscountRequest request);

    void saveDiscount(DiscountRequest request);

    boolean discountExists(int id);
}