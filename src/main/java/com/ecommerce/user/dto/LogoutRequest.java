package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for handling user logout requests.
 * This class is used to transfer the refresh token from the client to the server to be invalidated.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequest {
    /**
     * The refresh token to be invalidated.
     */
    private String refreshToken;
}
