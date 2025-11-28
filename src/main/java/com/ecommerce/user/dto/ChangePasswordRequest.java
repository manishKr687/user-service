
package com.ecommerce.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for handling password change requests.
 * This class is used to transfer the user's old and new passwords from the client to the server.
 * It includes validation annotations to ensure that the provided passwords meet the required constraints.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    /**
     * The user's current password.
     */
    @NotBlank(message = "Old password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String oldPassword;

    /**
     * The new password that the user wants to set.
     */
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;
}
