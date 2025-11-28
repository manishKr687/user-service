
package com.ecommerce.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for handling user login requests.
 * This class is used to transfer the user's credentials from the client to the server for authentication.
 * It includes validation annotations to ensure that the required fields are provided.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    /**
     * The user's email address.
     */
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * The user's password.
     */
    @NotBlank(message = "Password is required")
    private String password;
}
