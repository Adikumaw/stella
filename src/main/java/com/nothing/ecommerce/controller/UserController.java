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
    public ResponseEntity<String> registerUser(@RequestBody UserModel userModel) {
        userAdvanceService.registerUser(userModel);

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @GetMapping("/register/resend-token")
    public ResponseEntity<String> resendToken(@RequestBody String reference) {
        verificationTokenService.sender(reference);

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @GetMapping("/verify-user")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        boolean isVerified = userAdvanceService.verifyUser(token);

        if (isVerified) {
            return ResponseEntity.ok("Email verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }
    }

    @GetMapping("/verify-update")
    public ResponseEntity<String> verifyUserUpdate(@RequestParam("token") String token) {
        boolean isVerified = userAdvanceService.verifyUserUpdate(token);

        if (isVerified) {
            return ResponseEntity.ok("update verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }
    }

    @GetMapping
    public UserInfoModel getUserInfo(@RequestHeader("Authorization") String jwtHeader) {
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

            return userService.getUserInfo(reference);
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @PutMapping("/name")
    public UserInfoModel updateUserName(@RequestBody String name, @RequestHeader("Authorization") String jwtHeader) {
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

            int userId = userService.findUserIdByReference(reference);

            return userService.convertoInfoModel(userAdvanceService.updateUserName(userId, name));
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @PutMapping("/email")
    public ResponseEntity<String> updateUserEmail(@RequestBody String email,
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
            int userId = userService.findUserIdByReference(reference);

            userAdvanceService.updateUserEmail(userId, email);

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @PutMapping("/number")
    public ResponseEntity<String> updateUserNumber(@RequestBody String number,
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
            int userId = userService.findUserIdByReference(reference);

            userAdvanceService.updateUserNumber(userId, number);

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @PutMapping("/password")
    public ResponseEntity<String> updateUserPassword(@RequestBody UpdatePasswordRequest updateRequest,
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
            int userId = userService.findUserIdByReference(reference);

            userAdvanceService.updateUserPassword(userId, updateRequest.getCurrentPassword(),
                    updateRequest.getNewPassword());

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } else {
            throw new InvalidJWTHeaderException("invalid JWTHeader !!!");
        }
    }

    @PutMapping("/de-activate")
    public ResponseEntity<String> deactivateUser(@RequestBody String password,
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
            int userId = userService.findUserIdByReference(reference);

            userAdvanceService.deactivateUser(userId, password);

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
