
package com.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Represents a refresh token in the system.
 * Refresh tokens are used to obtain new JWT access tokens without requiring the user to log in again.
 * This entity is mapped to the "refresh_token" table in the database.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    /**
     * The unique identifier for the refresh token.
     * Using GenerationType.AUTO is portable, but for auto-incremented columns,
     * GenerationType.IDENTITY can be more efficient.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The refresh token string itself. This is a unique, randomly generated string.
     */
    private String token;

    /**
     * The date and time when this refresh token expires.
     */
    private Instant expiryDate;

    /**
     * The user associated with this refresh token.
     * This creates a one-to-one relationship with the User entity.
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User userInfo;
}
