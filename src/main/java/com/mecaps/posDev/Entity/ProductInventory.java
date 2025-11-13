package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
public class ProductInventory {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long inventory_id;

@DateTimeFormat
@CreationTimestamp
private LocalDateTime last_updated;

@Column(nullable = false)
private String location;

@Column(nullable = false)
private Long quantity;

@ManyToOne
private Product productId;

@OneToOne
private ProductVariant productVariant;
}
