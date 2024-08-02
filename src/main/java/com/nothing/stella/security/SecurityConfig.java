package com.nothing.stella.security;

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
                .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users/verify-user").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users/verify-update").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users/register/resend-token").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/test").permitAll()
                // SELLER ENDPOINTS
                .requestMatchers(HttpMethod.POST, "/api/sellers/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/sellers/upgrade").hasRole("BUYER")
                .requestMatchers(HttpMethod.POST, "/api/sellers/verify-update").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/sellers/store-name").hasRole("SELLER")
                .requestMatchers(HttpMethod.PUT, "/api/sellers/address").hasRole("SELLER")
                .requestMatchers(HttpMethod.POST, "/api/sellers/logo").hasRole("SELLER")
                .requestMatchers(HttpMethod.PUT, "/api/sellers/logo").hasRole("SELLER")
                // LOGIN ENDPOINTS
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                // TEST ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/api/testseller").hasRole("SELLER")
                .requestMatchers(HttpMethod.GET, "/api/testbuyer").hasRole("BUYER")
                // PRODUCT ENDPOINTS
                .requestMatchers(HttpMethod.POST, "/api/products").hasRole("SELLER")
                .requestMatchers(HttpMethod.PUT, "/api/products").hasRole("SELLER")
                .requestMatchers(HttpMethod.DELETE, "/api/products/de-activate").hasRole("SELLER")
                .requestMatchers(HttpMethod.POST, "/api/products/activate").hasRole("SELLER")
                // ORDER ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/api/orders/track").permitAll()
                // SELLER DASHBOARD ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/api/sellers/dashboard").hasRole("SELLER")
                .requestMatchers(HttpMethod.GET, "/api/sellers/dashboard/products").hasRole("SELLER")
                .requestMatchers(HttpMethod.POST, "/api/sellers/dashboard/update-order-status").hasRole("SELLER")
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
