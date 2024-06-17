package com.nothing.ecommerce.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "update_verification_token")
public class UpdateVerificationToken {
    private static final int EXPIRATION = 60; // time in minutes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "data")
    private String data;
    @Column(name = "token")
    private String token;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    public UpdateVerificationToken(String data, String token, int userId) {
        this.data = data;
        this.token = token;
        this.userId = userId;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private LocalDateTime calculateExpiryDate(int expiryTimeInMinutes) {
        // Get the current LocalDateTime
        LocalDateTime now = LocalDateTime.now();

        // Add the expiry time in minutes
        return now.plusMinutes(expiryTimeInMinutes);
    }
}
