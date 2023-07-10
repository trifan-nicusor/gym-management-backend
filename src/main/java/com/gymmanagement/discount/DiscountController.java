package com.gymmanagement.discount;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discounts")
public class DiscountController {

    private final DiscountServiceImpl discountService;

    @GetMapping
    public ResponseEntity<List<DiscountDTO>> getAllDisciplines() {
        List<DiscountDTO> discounts = discountService.getAllDiscounts();

        if (discounts.size() > 0) {
            return new ResponseEntity<>(discounts, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> saveDiscount(@RequestBody DiscountRequest request) {
        if (!discountService.discountExists(request.getName())) {
            discountService.saveDiscount(request);

            return new ResponseEntity<>("Discount successfully added!", HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateDiscount(@PathVariable int id,
                                                 @RequestBody DiscountRequest request) {
        if (discountService.discountExists(id)) {
            discountService.updateDiscount(id, request);
            return new ResponseEntity<>("Subscription successfully updated!", HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }
}