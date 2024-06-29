package com.nothing.ecommerce.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "seller")
public class Seller {

    @Id
    @Column(name = "user_id")
    private int userId;
    @Column(name = "store_name")
    private String storeName;
    @Column(name = "address")
    private String address;
    @Column(name = "logo")
    private String logo;
    @Column(name = "created_at")
    private Date createdAt;

    public Seller(int userId, String storeName, String address, String logo) {
        this.userId = userId;
        this.storeName = storeName;
        this.address = address;
        this.logo = logo;
        this.createdAt = new Date(); // Set current date and time
    }

    public Seller(int userId, String storeName, String address) {
        this.userId = userId;
        this.storeName = storeName;
        this.address = address;
        this.logo = null;
        this.createdAt = new Date(); // Set current date and time
    }
}
