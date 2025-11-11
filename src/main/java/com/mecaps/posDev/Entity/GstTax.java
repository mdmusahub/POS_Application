package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GstTax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Example: GST_18, GST_5, GST_12 etc.
    @Column(nullable = false, unique = true)
    private String gst_name;

    // Example: 5.0, 12.0, 18.0, 28.0
    @Column(nullable = false)
    private Double gst_rate;

    @Column(nullable = false)
    private Double c_gst;

    @Column(nullable = false)
    private Double s_gst;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category; // e.g., "Electronics", "Food", "Luxury"

}
