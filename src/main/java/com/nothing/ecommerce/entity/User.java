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

    public User(String userName, String email, String number, String password) {
        this.userName = userName;
        this.email = email;
        this.number = number;
        this.password = password;
        this.active = 1;
    }

    @Override
    public String toString() {
        return "User {\n\tuserId=" + userId + ", \n\tuserName=" + userName + ", \n\temail=" + email + ", \n\tnumber="
                + number
                + ", \n\tpassword=" + password + ", \n\tactive=" + active + "\n}";
    }
}
