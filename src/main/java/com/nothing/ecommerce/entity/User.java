package com.nothing.ecommerce.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "number")
    private String number;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private int active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAddress> addressList;

    public User(String userName, String email, String number, String password) {
        this.userName = userName;
        this.email = email;
        this.number = number;
        this.password = password;
        this.active = 1;
    }
}
