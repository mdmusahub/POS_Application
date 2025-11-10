package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.Discount;
import com.mecaps.posDev.Enums.WaiverMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DiscountResponse {

    private String discount_name;
    private Double discount_value;
    private LocalDateTime start_date_time;
    private LocalDateTime end_date_time;
    private Boolean is_active;
    private WaiverMode waiver_mode;
    private String product_variant;

    //  Constructor that accepts Discount entity and maps it to response
    public DiscountResponse(Discount discount) {
        this.discount_name = discount.getDiscount_name();
        this.discount_value = discount.getDiscount_value();
        this.is_active = discount.getIs_active();
        this.waiver_mode = discount.getWaiver_mode();
        this.product_variant = discount.getProductVariant().getVariantName();


    }
}
