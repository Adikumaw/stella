package com.nothing.ecommerce.entity;

import com.nothing.ecommerce.model.UserModel;

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
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "number")
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

    public User(UserModel userModel) {
        this.name = userModel.getUserName();
        this.email = userModel.getEmail();
        this.number = userModel.getNumber();
        this.password = userModel.getPassword();
        this.active = 1;
    }

    public int isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "User {\n\tuserId=" + userId + ", \n\tuserName=" + name + ", \n\temail=" + email + ", \n\tnumber="
                + number
                + ", \n\tpassword=" + password + ", \n\tactive=" + active + "\n}";
    }
}
