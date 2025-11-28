
package com.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a user's address in the system.
 * This entity is mapped to the "address" table in the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    /**
     * The unique identifier for the address.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The street name and number.
     */
    private String street;

    /**
     * The building or apartment name/number.
     */
    private String building;

    /**
     * The city.
     */
    private String city;

    /**
     * The state or province.
     */
    private String state;

    /**
     * The postal code or pincode.
     */
    private String pincode;

    /**
     * A flag to indicate if this is the user's default address.
     */
    private boolean isDefault;

    /**
     * The user associated with this address.
     * This creates a many-to-one relationship with the User entity.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
