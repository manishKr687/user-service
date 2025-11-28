
package com.ecommerce.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for handling new user registration requests.
 * This class is used to transfer the new user's information from the client to the server.
 * It includes validation annotations to ensure that the provided information is valid and meets the required constraints.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    /**
     * The user's full name.
     */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /**
     * The user's email address. Must be a valid email format.
     */
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    /**
     * The user's password. Must be at least 8 characters long and contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace")
    private String password;
}
