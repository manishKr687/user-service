
package com.ecommerce.user.service;

import com.ecommerce.user.entity.RefreshToken;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.TokenRefreshException;
import com.ecommerce.user.repository.RefreshTokenRepository;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * This service provides business logic for managing refresh tokens.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String REFRESH_TOKEN_EXPIRED = "Refresh token is expired. Please make a new login..!";

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    /**
     * Creates a new refresh token for a user.
     *
     * @param username The email address of the user.
     * @return The newly created {@link RefreshToken}.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        // Check if a refresh token already exists for the user
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserInfo(user);

        RefreshToken refreshToken;
        if (existingToken.isPresent()) {
            // Update the existing token
            refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
        } else {
            // Create a new token
            refreshToken = RefreshToken.builder()
                    .userInfo(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                    .build();
        }
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Finds a refresh token by its token string.
     *
     * @param token The token string to search for.
     * @return An {@link Optional} containing the found refresh token, or empty if not found.
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Verifies that a refresh token has not expired.
     * If the token is expired, it is deleted from the database and a {@link TokenRefreshException} is thrown.
     *
     * @param token The refresh token to verify.
     * @return The verified refresh token.
     * @throws TokenRefreshException if the refresh token is expired.
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), REFRESH_TOKEN_EXPIRED);
        }
        return token;
    }

    /**
     * Deletes a refresh token by its token string.
     *
     * @param token The token string of the refresh token to delete.
     */
    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
}
