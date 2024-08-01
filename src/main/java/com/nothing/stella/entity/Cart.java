package com.nothing.stella.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private int cartId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "amount")
    private Double totalAmount;

    public Cart(int userId, Double totalAmount) {
        this.userId = userId;
        this.totalAmount = totalAmount;
    }
}
