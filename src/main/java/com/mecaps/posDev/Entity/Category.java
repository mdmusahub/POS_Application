package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Product")
@Data

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
    private Long category_id;

    @Column(nullable = false, unique = true)
    private String category_name;

    @Column(nullable = false)
    private String category_description;
=======
   private Long id;
>>>>>>> 0c32961e4a3c0849b0845da4eccc22dd66fc3a0c


    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String product_name;

   @OneToMany
    private Category category;


}
