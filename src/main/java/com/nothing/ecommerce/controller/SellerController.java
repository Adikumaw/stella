package com.nothing.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nothing.ecommerce.exception.InvalidJWTHeaderException;
import com.nothing.ecommerce.exception.SellerException;
import com.nothing.ecommerce.exception.UnknownErrorException;
import com.nothing.ecommerce.exception.UserException;
import com.nothing.ecommerce.model.SellerInputModel;
import com.nothing.ecommerce.model.SellerUpgradeModel;
import com.nothing.ecommerce.model.SellerViewModel;
import com.nothing.ecommerce.services.JWTService;
import com.nothing.ecommerce.services.SellerService;

@RestController
@RequestMapping("/sellers")
public class SellerController {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private JWTService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SellerInputModel model) {
        sellerService.register(model);

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @PostMapping("/upgrade")
    public ResponseEntity<String> upgradeToSeller(@RequestBody SellerUpgradeModel model,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                sellerService.upgradeToSeller(reference, model);

                return ResponseEntity.status(HttpStatus.OK).body("Success");
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (UserException e) {
                throw e;
            } catch (SellerException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @GetMapping("/verify-update")
    public ResponseEntity<String> verifyUpdate(@RequestParam String token) {
        boolean isVerified = sellerService.verifyUpdate(token);

        if (isVerified) {
            return ResponseEntity.ok("update verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }
    }

    @PutMapping("/store-name")
    public ResponseEntity<String> updateStoreName(@RequestBody String storeName,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                sellerService.updateStoreName(reference, storeName);

                return ResponseEntity.status(HttpStatus.OK).body("Success");
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (UserException e) {
                throw e;
            } catch (SellerException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @PutMapping("/address")
    public SellerViewModel updateAddress(@RequestBody String address,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return sellerService.updateAddress(reference, address);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (UserException e) {
                throw e;
            } catch (SellerException e) {
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
