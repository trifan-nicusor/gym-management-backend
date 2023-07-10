package com.gymmanagement.discount;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService{

    private final DiscountRepository discountRepository;
    private final DiscountDTOMapper discountMapper;

    @Override
    public List<DiscountDTO> getAllDiscounts() {
        return discountRepository.findAllDiscounts()
                .stream()
                .map(discountMapper)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void saveDiscount(DiscountRequest request) {
        var discount = Discount.builder()
                .name(request.getName())
                .code(request.getCode())
                .type(request.getType())
                .usePerUser(request.getUsePerUser())
                .quantity(request.getQuantity())
                .compatibleWithOther(request.getCompatibleWithOther())
                .validFrom(request.getValidFrom())
                .minimumCartTotal(request.getMinimumCartTotal())
                .validTo(request.getValidTo())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        discountRepository.save(discount);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void updateDiscount(int subId, DiscountRequest request) {
        Discount discount = discountRepository.findById((long) subId).orElseThrow();

        if(request.getName() != null) {
            discount.setName(request.getName());
        }

        if(request.getCode() != null) {
            discount.setCode(request.getCode());
        }

        if(request.getType() != null) {
            discount.setType(request.getType());
        }

        if(request.getUsePerUser() != null) {
            discount.setUsePerUser(request.getUsePerUser());
        }

        if(request.getQuantity() != null) {
            discount.setQuantity(request.getQuantity());
        }

        if(request.getMinimumCartTotal() != null) {
            discount.setMinimumCartTotal(request.getMinimumCartTotal());
        }

        if(request.getCompatibleWithOther() != null) {
            discount.setCompatibleWithOther(request.getCompatibleWithOther());
        }

        if(request.getValidFrom() != null) {
            discount.setValidFrom(request.getValidFrom());
        }

        if(request.getValidTo() != null) {
            discount.setValidTo(request.getValidTo());
        }

        discountRepository.save(discount);
    }

    @Override
    public boolean discountExists(String name) {
        return discountRepository.getDiscountByName(name).isPresent();
    }

    @Override
    public boolean discountExists(int id) {
        return discountRepository.getDiscountById((long) id).isPresent();
    }
}
