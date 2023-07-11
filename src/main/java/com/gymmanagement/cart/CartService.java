package com.gymmanagement.cart;

import com.gymmanagement.cart.request.AddProductRequest;
import com.gymmanagement.cart.request.UpdateProductRequest;

public interface CartService {
    void addProduct(AddProductRequest request);

    CartDTO getCart();

    void deleteProduct(Long id);

    void updateProduct(Long id, UpdateProductRequest request);
}