package com.nothing.stella.entity;

import com.nothing.stella.model.AddressSaveModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    public Address(int userId, AddressSaveModel addressSaveModel) {
        this.userId = userId;
        this.streetAddress = addressSaveModel.getStreetAddress();
        this.city = addressSaveModel.getCity();
        this.state = addressSaveModel.getState();
        this.postalCode = addressSaveModel.getPostalCode();
        this.country = addressSaveModel.getCountry();
        this.main = addressSaveModel.getMain();
    }

    public String toString() {
        return streetAddress + ", " + city + ", " + state + " " + postalCode + ", " + country;
    }

    public Boolean isMain() {
        return this.main != 0 ? true : false;
    }
}
