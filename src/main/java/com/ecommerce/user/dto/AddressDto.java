
package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for representing address information.
 * This class is used to transfer address details between the server and the client.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    /**
     * The unique identifier for the address.
     */
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
}
