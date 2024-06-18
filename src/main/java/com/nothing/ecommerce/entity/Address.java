package com.nothing.ecommerce.entity;

import com.nothing.ecommerce.model.AddressModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

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

    public Boolean equals(AddressModel addressModel) {
        return this.streetAddress.equals(addressModel.getStreetAddress())
                && this.city.equals(addressModel.getCity())
                && this.state.equals(addressModel.getState())
                && this.postalCode.equals(addressModel.getPostalCode())
                && this.country.equals(addressModel.getCountry());
    }

    public Boolean isMain() {
        return this.main != 0 ? true : false;
    }
}
