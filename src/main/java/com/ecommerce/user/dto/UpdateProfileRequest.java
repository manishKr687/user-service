
package com.ecommerce.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for handling user profile update requests.
 * This class is used to transfer the user's updated profile information from the client to the server.
 * It includes validation annotations to ensure that required fields are provided.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    /**
     * The user's full name.
     */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /**
     * The user's phone number.
     */
    @NotBlank(message = "Phone number is required")
    private String phone;

    /**
     * The URL of the user's profile image.
     */
    private String profileImage;
}
