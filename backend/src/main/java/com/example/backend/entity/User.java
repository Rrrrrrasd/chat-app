package com.example.backend.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name= "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//기본키 - 자동증가
    private Long id;

    @Column(nullable = false)
    private String password;

    private String username;
    private String profileImage;
}
