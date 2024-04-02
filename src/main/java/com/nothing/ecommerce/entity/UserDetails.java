package com.nothing.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class UserDetails {
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

    @Column(name = "active")
    private int active;

    public UserDetails(String userName, String email, String number) {
        this.userName = userName;
        this.email = email;
        this.number = number;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                " user_id=" + this.userId +
                ", user_name='" + this.userName + '\'' +
                ", email='" + this.email + '\'' +
                ", number='" + this.number + '\'' +
                ", active='" + this.active + '\'' +
                '}';
    }
}
