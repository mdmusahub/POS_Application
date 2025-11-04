package com.mecaps.posDev.Entity;

import com.mecaps.posDev.Enums.WaiverMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discount_id;

    private String discount_name;
    private Double discount_value;

    @DateTimeFormat
    private LocalDateTime start_date_time;
    @DateTimeFormat
    private LocalDateTime end_date_time;

    private Boolean is_active;

    @Enumerated(EnumType.STRING)
    private WaiverMode waiver_mode;

    @OneToOne
    private ProductVariant product_variant;

}
