
package com.ecommerce.user.controller;

import com.ecommerce.user.dto.*;
import com.ecommerce.user.entity.RefreshToken;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.JwtService;
import com.ecommerce.user.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles authentication-related requests, such as user registration, login, token refreshing, and logout.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    /**
     * Registers a new user.
     *
     * @param request The request object containing user registration details.
     * @return A success message.
     * @throws RuntimeException if a user with the given email already exists.
     */
    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return "User Registered Successfully";
    }

    /**
     * Authenticates a user and returns a JWT and refresh token.
     *
     * @param request The request object containing login credentials.
     * @return A {@link JwtResponse} containing the JWT and refresh token.
     * @throws org.springframework.security.authentication.BadCredentialsException if the credentials are invalid.
     */
    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Invalid Credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid Credentials");
        }
        String token = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());
        return JwtResponse.builder().token(token).refreshToken(refreshToken.getToken()).build();
    }

    /**
     * Refreshes a JWT using a refresh token.
     *
     * @param refreshTokenRequest The request object containing the refresh token.
     * @return A {@link JwtResponse} containing a new JWT and the original refresh token.
     * @throws com.ecommerce.user.exception.TokenRefreshException if the refresh token is invalid or expired.
     */
    @PostMapping("/refresh")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getEmail());
                    return JwtResponse.builder()
                            .token(accessToken)
                            .refreshToken(refreshTokenRequest.getToken()).build();
                }).orElseThrow(() -> new com.ecommerce.user.exception.TokenRefreshException(refreshTokenRequest.getToken(), "Refresh Token is not in DB..!!"));
    }

    /**
     * Logs out a user by deleting their refresh token.
     *
     * @param logoutRequest The request object containing the refresh token.
     * @return A success message.
     */
    @PostMapping("/logout")
    public String logout(@RequestBody LogoutRequest logoutRequest) {
        refreshTokenService.deleteByToken(logoutRequest.getRefreshToken());
        return "Logout successful";
    }
}
