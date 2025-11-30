
package com.ecommerce.user.repository;

import com.ecommerce.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface defines the repository for the {@link User} entity.
 * It provides methods for CRUD operations and custom queries related to users.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for.
     * @return An {@link Optional} containing the found user, or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given role exists.
     *
     * @param role The role to check for.
     * @return {@code true} if a user with the role exists, {@code false} otherwise.
     */
    boolean existsByRole(String role);
}
