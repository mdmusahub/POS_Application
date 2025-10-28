package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Product")
@Data

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;


    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String product_name;

   @OneToMany
    private Category category;


}
