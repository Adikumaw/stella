package com.nothing.ecommerce.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerUpgradeModel {
    private String storeName;
    private String address;
    private LocalDateTime createdAt;
}
