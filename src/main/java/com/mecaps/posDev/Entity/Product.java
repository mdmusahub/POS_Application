package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    @DateTimeFormat
    @CreationTimestamp
    private LocalDateTime created_at;

    @DateTimeFormat
    @UpdateTimestamp
    private LocalDateTime updated_at;
}
