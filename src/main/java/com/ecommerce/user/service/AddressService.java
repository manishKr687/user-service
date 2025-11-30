
package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddressDto;
import com.ecommerce.user.dto.AddressRequest;
import com.ecommerce.user.entity.Address;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.AddressRepository;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * This service provides business logic for managing user addresses.
 */
@Service
@RequiredArgsConstructor
public class AddressService {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String ADDRESS_NOT_FOUND = "Address not found";

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves all addresses for a given user.
     *
     * @param username The email address of the user.
     * @return A list of {@link AddressDto} objects.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public List<AddressDto> getAddresses(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        return user.getAddresses().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * Adds a new address for a user.
     *
     * @param username The email address of the user.
     * @param request  The request object containing the new address details.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public void addAddress(String username, AddressRequest request) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        Address address = new Address();
        mapToEntity(request, address);
        address.setUser(user);
        if (request.isDefault()) {
            user.getAddresses().forEach(a -> a.setDefault(false));
        }
        addressRepository.save(address);
    }

    /**
     * Updates an existing address.
     *
     * @param addressId The ID of the address to update.
     * @param request   The request object containing the updated address details.
     * @throws RuntimeException if the address is not found.
     */
    public void updateAddress(Long addressId, AddressRequest request) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException(ADDRESS_NOT_FOUND));
        mapToEntity(request, address);
        if (request.isDefault()) {
            address.getUser().getAddresses().forEach(a -> a.setDefault(false));
            address.setDefault(true);
        }
        addressRepository.save(address);
    }

    /**
     * Deletes an address.
     *
     * @param addressId The ID of the address to delete.
     */
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    /**
     * Sets an address as the default for the user.
     *
     * @param addressId The ID of the address to set as default.
     * @throws RuntimeException if the address is not found.
     */
    public void setDefaultAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException(ADDRESS_NOT_FOUND));
        address.getUser().getAddresses().forEach(a -> a.setDefault(false));
        address.setDefault(true);
        addressRepository.save(address);
    }

    /**
     * Maps an {@link Address} entity to an {@link AddressDto}.
     *
     * @param address The address entity.
     * @return The mapped address DTO.
     */
    private AddressDto mapToDto(Address address) {
        return new AddressDto(address.getId(), address.getStreet(), address.getBuilding(), address.getCity(), address.getState(), address.getPincode(), address.isDefault());
    }

    /**
     * Maps an {@link AddressRequest} to an {@link Address} entity.
     *
     * @param request The address request DTO.
     * @param address The address entity to map to.
     */
    private void mapToEntity(AddressRequest request, Address address) {
        address.setStreet(request.getStreet());
        address.setBuilding(request.getBuilding());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setDefault(request.isDefault());
    }
}
