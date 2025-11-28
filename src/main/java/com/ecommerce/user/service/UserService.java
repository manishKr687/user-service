
package com.ecommerce.user.service;

import com.ecommerce.user.dto.ChangePasswordRequest;
import com.ecommerce.user.dto.UpdateProfileRequest;
import com.ecommerce.user.dto.UserProfileDto;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * This service provides business logic for user-related operations, such as managing user profiles and changing passwords.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves the profile of a user.
     *
     * @param username The email address of the user.
     * @return A {@link UserProfileDto} object containing the user's profile information.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public UserProfileDto getUserProfile(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserProfileDto(user.getFullName(), user.getEmail(), user.getPhone(), user.getProfileImage());
    }

    /**
     * Updates the profile of a user.
     *
     * @param username The email address of the user.
     * @param request  The request object containing the updated profile information.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public void updateUserProfile(String username, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setProfileImage(request.getProfileImage());
        userRepository.save(user);
    }

    /**
     * Changes the password of a user.
     *
     * @param username The email address of the user.
     * @param request  The request object containing the old and new passwords.
     * @throws UsernameNotFoundException  if the user is not found.
     * @throws IllegalArgumentException if the old password is invalid.
     */
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
