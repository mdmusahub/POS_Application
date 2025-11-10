package com.mecaps.posDev.Request;

import com.mecaps.posDev.Enums.WaiverMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DiscountRequest {
    private String discount_name;
    private Double discount_value;
    private LocalDateTime start_date_time;
    private LocalDateTime end_date_time;
    private Boolean is_active;
    private WaiverMode waiver_mode;
    private Long product_variant;
}
