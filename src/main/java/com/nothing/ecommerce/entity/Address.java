package com.nothing.ecommerce.entity;

import com.nothing.ecommerce.model.AddressModel;

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
public class Address {
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

    @Column(name = "main")
    private int main;

    public Address(int userId, AddressModel addressModel) {
        this.userId = userId;
        this.streetAddress = addressModel.getStreetAddress();
        this.city = addressModel.getCity();
        this.state = addressModel.getState();
        this.postalCode = addressModel.getPostalCode();
        this.country = addressModel.getCountry();
        this.main = addressModel.getMain();
    }

    @Override
    public String toString() {
        return "UserAddress {\n\tuserId=" + userId + ", \n\tstreetAddress=" + streetAddress + ", \n\tcity="
                + city + ", \n\tstate=" + state + ", \n\tpostalCode=" + postalCode + ", \n\tcountry=" + country
                + ", \n\tmain=" + main
                + "\n}";
    }
}
