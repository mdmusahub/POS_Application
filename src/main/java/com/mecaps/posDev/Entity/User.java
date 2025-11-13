package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "userClass")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    @Column(nullable = false , unique = true)
    private String email ;
    @Column(nullable = false)
    private String password ;
    private boolean active ;
    @CreationTimestamp
    @DateTimeFormat
    private LocalDateTime created_at ;
    @Column(nullable = false)
    private String role;


}
