
package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for handling refresh token requests.
 * This class is used to send a refresh token from the client to the server to obtain a new access token.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    /**
     * The refresh token string.
     */
    private String token;
}
