package com.nothing.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AddressId.class)
@Table(name = "address")
public class UserAddress {
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    public UserAddress(String streetAddress, String city, String state, String postalCode, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    @Override
    public String toString() {
        return "UserAddress {\n\tuserId=" + userId + ", \n\tstreetAddress=" + streetAddress + ", \n\tcity="
                + city + ", \n\tstate=" + state + ", \n\tpostalCode=" + postalCode + ", \n\tcountry=" + country
                + "\n}";
    }
}
