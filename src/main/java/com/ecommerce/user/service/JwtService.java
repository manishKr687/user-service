package com.ecommerce.user.service;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This service provides utility methods for working with JSON Web Tokens (JWTs).
 * It handles the generation and validation of JWTs.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String ROLE_CLAIM = "role";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private final UserRepository userRepository;

    /**
     * Generates a new JWT for the given user.
     *
     * @param user The user for whom to generate the token.
     * @return A new JWT string.
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, user.getRole()); // Include the user's role in claims
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username from a JWT.
     *
     * @param token The JWT string.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates a JWT.
     *
     * @param token       The JWT string.
     * @param userDetails The user details to validate against.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final Claims claims = extractAllClaims(token);
        final String username = claims.getSubject();
        final Date expiration = claims.getExpiration();
        return username.equals(userDetails.getUsername()) && !expiration.before(new Date());
    }

    /**
     * Extracts a specific claim from a JWT.
     *
     * @param token          The JWT string.
     * @param claimsResolver A function to extract the claim.
     * @param <T>            The type of the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private java.security.Key getSigningKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secret);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }
}