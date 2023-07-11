package com.gymmanagement.cart;

import com.gymmanagement.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long id;
    private List<ProductDTO> products;
    private Long total;
}
