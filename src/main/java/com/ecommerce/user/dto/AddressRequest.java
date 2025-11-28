
package com.ecommerce.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for creating or updating a user's address.
 * This class is used to transfer address information from the client to the server.
 * It includes validation annotations to ensure that the required fields are provided.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    /**
     * The street name and number.
     */
    @NotBlank(message = "Street is required")
    private String street;

    /**
     * The building or apartment name/number.
     */
    @NotBlank(message = "Building is required")
    private String building;

    /**
     * The city.
     */
    @NotBlank(message = "City is required")
    private String city;

    /**
     * The state or province.
     */
    @NotBlank(message = "State is required")
    private String state;

    /**
     * The postal code or pincode.
     */
    @NotBlank(message = "Pincode is required")
    private String pincode;

    /**
     * A flag to indicate if this should be the user's default address.
     */
    private boolean isDefault;
}
