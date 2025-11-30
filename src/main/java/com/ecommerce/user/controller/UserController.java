

package com.ecommerce.user.controller;

import com.ecommerce.user.dto.AddressDto;
import com.ecommerce.user.dto.AddressRequest;
import com.ecommerce.user.dto.ChangePasswordRequest;
import com.ecommerce.user.dto.UpdateProfileRequest;
import com.ecommerce.user.dto.UserProfileDto;
import com.ecommerce.user.service.AddressService;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "the user API")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    /**
     * Retrieves the profile of the currently authenticated user.
     *
     * @param principal The currently authenticated user.
     * @return A {@link UserProfileDto} containing the user's profile information.
     */
    @Operation(summary = "Get user profile", description = "Retrieves the profile of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping("/profile")
    public UserProfileDto getProfile(@Parameter(hidden = true) Principal principal) {
        return userService.getUserProfile(principal.getName());
    }

    /**
     * Updates the profile of the currently authenticated user.
     *
     * @param principal The currently authenticated user.
     * @param request   The request object containing the updated profile information.
     */
    @Operation(summary = "Update user profile", description = "Updates the profile of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated user profile"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PutMapping("/profile")
    public void updateProfile(@Parameter(hidden = true) Principal principal, @Valid @RequestBody UpdateProfileRequest request) {
        userService.updateUserProfile(principal.getName(), request);
    }

    /**
     * Changes the password of the currently authenticated user.
     *
     * @param principal The currently authenticated user.
     * @param request   The request object containing the old and new passwords.
     */
    @Operation(summary = "Change user password", description = "Changes the password of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed user password"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PostMapping("/password")
    public void changePassword(@Parameter(hidden = true) Principal principal, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(principal.getName(), request);
    }

    /**
     * Retrieves all addresses for the currently authenticated user.
     *
     * @param principal The currently authenticated user.
     * @return A list of {@link com.ecommerce.user.dto.AddressDto} objects.
     */
    @Operation(summary = "Get user addresses", description = "Retrieves all addresses for the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user addresses"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @GetMapping("/address")
    public List<AddressDto> getAddresses(@Parameter(hidden = true) Principal principal) {
        return addressService.getAddresses(principal.getName());
    }

    /**
     * Adds a new address for the currently authenticated user.
     *
     * @param principal The currently authenticated user.
     * @param request   The request object containing the new address details.
     */
    @Operation(summary = "Add a new address", description = "Adds a new address for the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a new address"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PostMapping("/address")
    public void addAddress(@Parameter(hidden = true) Principal principal, @Valid @RequestBody AddressRequest request) {
        addressService.addAddress(principal.getName(), request);
    }

    /**
     * Updates an existing address.
     *
     * @param id      The ID of the address to update.
     * @param request The request object containing the updated address details.
     */
    @Operation(summary = "Update an address", description = "Updates an existing address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the address"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PutMapping("/address/{id}")
    public void updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequest request) {
        addressService.updateAddress(id, request);
    }

    /**
     * Deletes an address.
     *
     * @param id The ID of the address to delete.
     */
    @Operation(summary = "Delete an address", description = "Deletes an address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the address"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @DeleteMapping("/address/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }

    /**
     * Sets an address as the default for the user.
     *
     * @param id The ID of the address to set as default.
     */
    @Operation(summary = "Set a default address", description = "Sets an address as the default for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set the default address"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    @PutMapping("/address/default/{id}")
    public void setDefaultAddress(@PathVariable Long id) {
        addressService.setDefaultAddress(id);
    }
}

