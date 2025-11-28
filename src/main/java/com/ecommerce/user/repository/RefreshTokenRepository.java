
package com.ecommerce.user.repository;

import com.ecommerce.user.entity.RefreshToken;
import com.ecommerce.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface defines the repository for the {@link RefreshToken} entity.
 * It provides methods for CRUD operations and custom queries related to refresh tokens.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Finds a refresh token by its token string.
     *
     * @param token The token string to search for.
     * @return An {@link Optional} containing the found refresh token, or empty if not found.
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Deletes all refresh tokens associated with a specific user.
     *
     * @param user The user whose refresh tokens should be deleted.
     */
    void deleteByUserInfo(User user);
}
