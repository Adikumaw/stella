package com.nothing.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressModel {

    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private int main;

    public Boolean isMain() {
        return this.main != 0 ? true : false;
    }
}
