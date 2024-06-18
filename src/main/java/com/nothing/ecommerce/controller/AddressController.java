package com.nothing.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nothing.ecommerce.exception.InvalidJWTHeaderException;
import com.nothing.ecommerce.model.AddressModel;
import com.nothing.ecommerce.model.AddressUpdateRequest;
import com.nothing.ecommerce.services.AddressService;
import com.nothing.ecommerce.services.JWTService;

@RestController
@RequestMapping("/users/address")
public class AddressController {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private AddressService addressService;

    @PostMapping
    public List<AddressModel> save(@RequestBody AddressModel addressModel,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            String reference = null;

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                reference = jwtService.fetchReference(jwtToken);
            } catch (Exception e) {
                System.out.println("------------------------->>>>");
                System.out.println(e.getMessage());
            }

            return addressService.save(reference, addressModel);
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @GetMapping
    public List<AddressModel> getAddresses(@RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            String reference = null;

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                reference = jwtService.fetchReference(jwtToken);
            } catch (Exception e) {
                System.out.println("------------------------->>>>");
                System.out.println(e.getMessage());
            }

            return addressService.getAddressModels(reference);
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader!!!");
        }
    }

    @PutMapping
    public List<AddressModel> update(@RequestBody AddressUpdateRequest request,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            String reference = null;

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                reference = jwtService.fetchReference(jwtToken);
            } catch (Exception e) {
                System.out.println("------------------------->>>>");
                System.out.println(e.getMessage());
            }

            AddressModel oldAddress = request.getOldAddress();
            AddressModel newAddress = request.getNewAddress();

            if (oldAddress != null && newAddress != null) {
                if (oldAddress != newAddress) {
                    return addressService.update(reference, oldAddress, newAddress);
                } else {
                    return addressService.getAddressModels(reference);
                }
            } else {
                throw new IllegalArgumentException("wrong address..");
            }

        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader!!!");
        }
    }

    @DeleteMapping
    public List<AddressModel> delete(@RequestBody AddressModel addressModel,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            String reference = null;

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                reference = jwtService.fetchReference(jwtToken);
            } catch (Exception e) {
                System.out.println("------------------------->>>>");
                System.out.println(e.getMessage());
            }

            return addressService.delete(reference, addressModel);
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader!!!");
        }
    }

}
