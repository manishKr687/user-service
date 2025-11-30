package com.ecommerce.user.controller;

import com.ecommerce.user.dto.UpdateUserRoleRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // All methods in this controller require ADMIN role
public class AdminController {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found with ID: ";

    private final UserRepository userRepository;
    private final JwtService jwtService; // Potentially useful for admin tasks, though not directly used in these examples

    /**
     * Retrieves a list of all users. Accessible only by users with the ADMIN role.
     *
     * @return A list of {@link UserResponse} objects for all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /**
     * Updates the role of a specific user. Accessible only by users with the ADMIN role.
     *
     * @param userId The ID of the user whose role is to be updated.
     * @param request The request body containing the new role.
     * @return A {@link UserResponse} object of the updated user.
     */
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<UserResponse> updateUserRole(@PathVariable Long userId, @Valid @RequestBody UpdateUserRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE + userId));

        user.setRole(request.getRole());
        User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(UserResponse.builder()
                .id(updatedUser.getId())
                .fullName(updatedUser.getFullName())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole())
                .build());
    }
}
