package com.nothing.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                // USER ENDPOINTS
                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/verify-user").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/verify-update").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/register/resend-token").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/test").permitAll()
                // SELLER ENDPOINTS
                .requestMatchers(HttpMethod.POST, "/sellers/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/sellers/upgrade").hasRole("BUYER")
                .requestMatchers(HttpMethod.GET, "/sellers/verify-update").permitAll()
                .requestMatchers(HttpMethod.PUT, "/sellers/store-name").hasRole("SELLER")
                .requestMatchers(HttpMethod.PUT, "/sellers/address").hasRole("SELLER")
                .requestMatchers(HttpMethod.POST, "/sellers/logo").hasRole("SELLER")
                .requestMatchers(HttpMethod.PUT, "/sellers/logo").hasRole("SELLER")
                // LOGIN ENDPOINTS
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                // TEST ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/testseller").hasRole("SELLER")
                .requestMatchers(HttpMethod.GET, "/testbuyer").hasRole("BUYER")
                // PRODUCT ENDPOINTS
                .requestMatchers(HttpMethod.POST, "/products").hasRole("SELLER")
                .requestMatchers(HttpMethod.PUT, "/products").hasRole("SELLER")
                .requestMatchers(HttpMethod.DELETE, "/products/de-activate").hasRole("SELLER")
                .requestMatchers(HttpMethod.POST, "/products/activate").hasRole("SELLER")
                // ORDER ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/orders/track").permitAll()
                .anyRequest().authenticated());

        // transfering exception control to JWTAuthenticationEntryPoint
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        // stateless makes security to verify user every time
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // add JWT authentication filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // disabling csrf
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    // ... other security configuration methods
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
