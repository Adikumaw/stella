package com.nothing.ecommerce.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nothing.ecommerce.services.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        String reference = null;
        String token = null;

        if (jwtService.verifyJwtHeader(requestHeader)) {
            // extract token from request header
            token = requestHeader.substring(7);
            try {
                reference = this.jwtService.fetchReference(token);
            } catch (Exception e) {
                logger.error("Reference Fetcher failed: " + e.getMessage());
            }
        }

        if (reference != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // fetch user detail from reference
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(reference);
            Boolean validateToken = this.jwtService.validateToken(token, userDetails);

            if (validateToken) {
                // set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // add Request Details to authentication
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set the authentication
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.error("Invalid Token request: " + token);
            }
        }
        // pass further filteration to Spring Security
        filterChain.doFilter(request, response);
    }

}
