package com.nothing.stella.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nothing.stella.exception.ImageException;
import com.nothing.stella.exception.InvalidJWTHeaderException;
import com.nothing.stella.exception.SellerException;
import com.nothing.stella.exception.UnknownErrorException;
import com.nothing.stella.exception.UserException;
import com.nothing.stella.model.SellerInputModel;
import com.nothing.stella.model.SellerUpgradeModel;
import com.nothing.stella.model.SellerViewModel;
import com.nothing.stella.services.JWTService;
import com.nothing.stella.services.SellerService;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private JWTService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SellerInputModel model) {
        try {

            if (sellerService.register(model)) {
                return ResponseEntity.status(HttpStatus.OK).body("Success");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register");
            }

        } catch (UserException e) {
            throw e;
        } catch (SellerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unknown error: " + e.getMessage(), e);
            throw new UnknownErrorException("Error: unknown error");
        }
    }

    @PostMapping("/upgrade")
    public ResponseEntity<String> upgradeToSeller(@RequestBody SellerUpgradeModel model,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                if (sellerService.upgradeToSeller(reference, model)) {
                    return ResponseEntity.status(HttpStatus.OK).body("Success");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failded to Upgrade");
                }
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

    @PostMapping("/verify-update")
    public ResponseEntity<String> verifyUpdate(@RequestParam String token) {
        try {
            boolean isVerified = sellerService.verifyUpdate(token);

            if (isVerified) {
                return ResponseEntity.ok("update verified successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
            }

        } catch (UserException e) {
            throw e;
        } catch (SellerException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unknown error: " + e.getMessage(), e);
            throw new UnknownErrorException("Error: unknown error");
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

    @PostMapping("/logo")
    public SellerViewModel addLogo(@RequestParam("logo") MultipartFile logoFile,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return sellerService.addLogo(reference, logoFile);
            } catch (UserException e) {
                throw e;
            } catch (SellerException e) {
                throw e;
            } catch (ImageException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Unknown error: " + e.getMessage(), e);
                throw new UnknownErrorException("Error: unknown error");
            }
        } else {
            throw new InvalidJWTHeaderException("Error: Invalid JWTHeader");
        }
    }

    @PutMapping("/logo")
    public SellerViewModel updateLogo(@RequestParam("logo") MultipartFile logoFile,
            @RequestHeader("Authorization") String jwtHeader) {
        if (jwtService.verifyJwtHeader(jwtHeader)) {
            // extract token from request header
            String jwtToken = jwtHeader.substring(7);
            try {
                String reference = jwtService.fetchReference(jwtToken);

                return sellerService.addLogo(reference, logoFile);
            } catch (UserException e) {
                throw e;
            } catch (SellerException e) {
                throw e;
            } catch (ImageException e) {
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
