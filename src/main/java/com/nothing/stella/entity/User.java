package com.nothing.stella.entity;

import com.nothing.stella.model.UserInputModel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "number", unique = true)
    private String number;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private int active;

    public User(String name, String email, String number, String password) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
        this.active = 1;
    }

    public User(UserInputModel userModel) {
        this.name = userModel.getName();
        this.email = userModel.getEmail();
        this.number = userModel.getNumber();
        this.password = userModel.getPassword();
        this.active = 1;
    }

    public boolean isActive() {
        return active != 0 ? true : false;
    }
}
