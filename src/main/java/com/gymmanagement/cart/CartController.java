package com.gymmanagement.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartServiceImpl cartService;

    @PostMapping
    public void addProduct(@RequestBody CartRequest request) {
        cartService.addProduct(request);
    }
}