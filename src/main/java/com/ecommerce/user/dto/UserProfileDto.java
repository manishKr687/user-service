
package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for sending user profile information to the client.
 * This class provides a client-friendly representation of a user's profile.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    /**
     * The user's full name.
     */
    private String fullName;
    /**
     * The user's email address.
     */
    private String email;
    /**
     * The user's phone number.
     */
    private String phone;
    /**
     * The URL of the user's profile image.
     */
    private String profileImage;
}
