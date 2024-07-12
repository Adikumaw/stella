package com.nothing.ecommerce.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nothing.ecommerce.exception.InvalidJWTHeaderException;
import com.nothing.ecommerce.exception.UnknownErrorException;
import com.nothing.ecommerce.exception.UserException;
import com.nothing.ecommerce.model.AddressSaveModel;
import com.nothing.ecommerce.model.AddressViewModel;
import com.nothing.ecommerce.services.AddressService;
import com.nothing.ecommerce.services.JWTService;

@RestController
@RequestMapping("/users/address")
public class AddressController {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private AddressService addressService;

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @PostMapping
    public List<AddressViewModel> save(@RequestBody AddressSaveModel addressModel,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            String reference = null;

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                reference = jwtService.fetchReference(jwtToken);
                return addressService.save(reference, addressModel);
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @GetMapping
    public List<AddressViewModel> getAddresses(@RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            String reference = null;

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                reference = jwtService.fetchReference(jwtToken);

                return addressService.getAddressModels(reference);
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @PutMapping
    public List<AddressViewModel> update(@RequestBody AddressViewModel updateRequest,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            String reference = null;

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                reference = jwtService.fetchReference(jwtToken);

                return addressService.update(reference, updateRequest);
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else

        {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @DeleteMapping
    public List<AddressViewModel> delete(@RequestParam int addressId,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            String reference = null;

            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                reference = jwtService.fetchReference(jwtToken);

                return addressService.delete(reference, addressId);
            } catch (UserException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

}
