package com.nothing.ecommerce.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nothing.ecommerce.exception.InvalidJWTHeaderException;
import com.nothing.ecommerce.model.UpdatePasswordRequest;
import com.nothing.ecommerce.model.UserInfoModel;
import com.nothing.ecommerce.model.UserModel;
import com.nothing.ecommerce.services.UserService;
import com.nothing.ecommerce.services.JWTService;
import com.nothing.ecommerce.services.UserAdvanceService;
import com.nothing.ecommerce.services.VerificationTokenService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserAdvanceService userAdvanceService;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserModel userModel) {
        userAdvanceService.register(userModel);

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @GetMapping("/register/resend-token")
    public ResponseEntity<String> resendToken(@RequestBody String reference) {
        verificationTokenService.sender(reference);

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @GetMapping("/verify-user")
    public ResponseEntity<String> verify(@RequestParam("token") String token) {
        boolean isVerified = userAdvanceService.verify(token);

        if (isVerified) {
            return ResponseEntity.ok("Email verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }
    }

    @GetMapping("/verify-update")
    public ResponseEntity<String> verifyUpdate(@RequestParam("token") String token) {
        boolean isVerified = userAdvanceService.verifyUpdate(token);

        if (isVerified) {
            return ResponseEntity.ok("update verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }
    }

    @GetMapping
    public UserInfoModel getInfo(@RequestHeader("Authorization") String jwtHeader) {
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

            return userService.getInfo(reference);
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @PutMapping("/name")
    public UserInfoModel updateName(@RequestBody String name, @RequestHeader("Authorization") String jwtHeader) {
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

            return userService.convertoInfoModel(userAdvanceService.updateName(reference, name));
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @PutMapping("/email")
    public ResponseEntity<String> updateEmail(@RequestBody String email,
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

            userAdvanceService.updateEmail(reference, email);

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @PutMapping("/number")
    public ResponseEntity<String> updateNumber(@RequestBody String number,
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

            userAdvanceService.updateNumber(reference, number);

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest updateRequest,
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

            userAdvanceService.updatePassword(reference, updateRequest.getCurrentPassword(),
                    updateRequest.getNewPassword());

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @PutMapping("/de-activate")
    public ResponseEntity<String> deactivate(@RequestBody String password,
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

            userAdvanceService.deactivate(reference, password);

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String jwtHeader) {
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

            userAdvanceService.delete(reference);

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @GetMapping("/hello")
    public String hello_world() {
        return "hello world!";
    }

}
