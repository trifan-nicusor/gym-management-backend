package com.gymmanagement.cart;

import com.gymmanagement.cart.request.AddProductRequest;
import com.gymmanagement.cart.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartServiceImpl cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getAllSubscriptions() {
        CartDTO cart = cartService.getCart();

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping
    public void addProduct(@RequestBody AddProductRequest request) {
        cartService.addProduct(request);
    }

    @PatchMapping("/{id}")
    public void updateProduct(@PathVariable Long id,
                              @RequestBody UpdateProductRequest request) {
        cartService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        cartService.deleteProduct(id);
    }
}