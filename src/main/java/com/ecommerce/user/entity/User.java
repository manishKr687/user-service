
package com.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a user in the system.
 * This is the central entity for user information and authentication.
 * This entity is mapped to the "users" table in the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user's full name.
     */
    private String fullName;

    /**
     * The user's email address. This is used as the username for authentication and must be unique.
     */
    @Column(unique = true)
    private String email;

    /**
     * The user's hashed password.
     */
    private String password;

    /**
     * The user's phone number.
     */
    private String phone;

    /**
     * The URL of the user's profile image.
     */
    private String profileImage;

    /**
     * The role of the user. Defaults to "USER".
     * In a more complex application, this might be an enum or a separate entity.
     */
    @Builder.Default
    private String role = "USER";

    /**
     * The list of addresses associated with the user.
     * This creates a one-to-many relationship with the Address entity.
     * CascadeType.ALL means that operations (persist, remove, etc.) on a User will be cascaded to their Addresses.
     * orphanRemoval = true means that if an Address is removed from this list, it will be deleted from the database.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Address> addresses;
}
