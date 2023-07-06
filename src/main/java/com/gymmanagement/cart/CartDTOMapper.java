package com.gymmanagement.cart;

import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
public class CartDTOMapper implements Function<Cart, CartDTO> {
    @Override
    public CartDTO apply(Cart cart) {
        return new CartDTO(
                cart.getId(),
                cart.getOrdered(),
                cart.getCreatedAt()
        );
    }
}