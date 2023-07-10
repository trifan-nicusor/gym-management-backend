package com.gymmanagement.discount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequest {
    private String name;
    private String code;
    private String type;
    private Long usePerUser;
    private Long quantity;
    private Long minimumCartTotal;
    private Boolean compatibleWithOther;
    private Date validFrom;
    private Date validTo;
}
