
package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) for returning authentication tokens to the client.
 * This class holds the access token (JWT) and the refresh token.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    /**
     * The JSON Web Token (JWT) used for authenticating requests.
     */
    private String token;
    /**
     * The refresh token used to obtain a new JWT when the current one expires.
     */
    private String refreshToken;
}
