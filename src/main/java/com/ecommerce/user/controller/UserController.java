

package com.ecommerce.user.controller;

import com.ecommerce.user.dto.AddressRequest;
import com.ecommerce.user.dto.ChangePasswordRequest;
import com.ecommerce.user.dto.UpdateProfileRequest;
import com.ecommerce.user.dto.UserProfileDto;
import com.ecommerce.user.service.AddressService;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * This controller handles requests related to user profile and address management.
 * All endpoints in this controller require an authenticated user.
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    /**
     * Retrieves the profile of the currently authenticated user.
     *
     * @param principal The currently authenticated user.
     * @return A {@link UserProfileDto} containing the user's profile information.
     */
    @GetMapping("/profile")
    public UserProfileDto getProfile(Principal principal) {
        return userService.getUserProfile(principal.getName());
    }

    /**
     * Updates the profile of the currently authenticated user.
     *
     * @param principal The currently authenticated user.
     * @param request   The request object containing the updated profile information.
     */
    @PutMapping("/profile")
    public void updateProfile(Principal principal, @Valid @RequestBody UpdateProfileRequest request) {
        userService.updateUserProfile(principal.getName(), request);
    }

    /**
     * Changes the password of the currently authenticated user.
     *
     * @param principal The currently authenticated user.
     * @param request   The request object containing the old and new passwords.
     */
    @PostMapping("/password")
    public void changePassword(Principal principal, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(principal.getName(), request);
    }

    /**
     * Retrieves all addresses for the currently authenticated user.
     *
     * @param principal The currently authenticated user.
     * @return A list of {@link com.ecommerce.user.dto.AddressDto} objects.
     */
    @GetMapping("/address")
    public List<com.ecommerce.user.dto.AddressDto> getAddresses(Principal principal) {
        return addressService.getAddresses(principal.getName());
    }

    /**
     * Adds a new address for the currently authenticated user.
     *
     * @param principal The currently authenticated user.
     * @param request   The request object containing the new address details.
     */
    @PostMapping("/address")
    public void addAddress(Principal principal, @Valid @RequestBody AddressRequest request) {
        addressService.addAddress(principal.getName(), request);
    }

    /**
     * Updates an existing address.
     *
     * @param id      The ID of the address to update.
     * @param request The request object containing the updated address details.
     */
    @PutMapping("/address/{id}")
    public void updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequest request) {
        addressService.updateAddress(id, request);
    }

    /**
     * Deletes an address.
     *
     * @param id The ID of the address to delete.
     */
    @DeleteMapping("/address/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }

    /**
     * Sets an address as the default for the user.
     *
     * @param id The ID of the address to set as default.
     */
    @PutMapping("/address/default/{id}")
    public void setDefaultAddress(@PathVariable Long id) {
        addressService.setDefaultAddress(id);
    }
}

