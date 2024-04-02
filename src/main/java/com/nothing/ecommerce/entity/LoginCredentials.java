package com.nothing.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class LoginCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "email")
    private String email;

    @Column(name = "number")
    private String number;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private int active;

    public LoginCredentials(String password) {
        this.password = password;
    }

}
