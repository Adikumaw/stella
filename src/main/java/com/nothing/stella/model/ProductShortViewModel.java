package com.nothing.stella.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductShortViewModel {
    private int id;
    private String name;
    private String description;
    private double price;
}
