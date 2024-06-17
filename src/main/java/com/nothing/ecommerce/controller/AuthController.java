package com.nothing.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.model.JWTRequest;
import com.nothing.ecommerce.model.JWTResponse;
import com.nothing.ecommerce.security.CustomUserDetailsService;
import com.nothing.ecommerce.services.JWTService;
import com.nothing.ecommerce.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "https://localhost")
public class AuthController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {
        // Authenticate username and password
        this.doAuthenticate(request.getReference(), request.getPassword());
        // Fetch user details after authenticating
        UserDetails userDetails = null;
        try {
            userDetails = customUserDetailsService.loadUserByUsername(request.getReference());
        } catch (DisabledException e) {
            // set user Active
            User user = userService.get(request.getReference());
            user.setActive(1);
            userService.save(user);

            return login(request);
        }
        // generate Jwt token
        String token = jwtService.generateToken(userDetails);
        // return response token
        JWTResponse response = JWTResponse.builder()
                .jwtToken(token)
                .reference(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String reference, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(reference,
                password);
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password !!");
        }
    }
}
