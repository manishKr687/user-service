
package com.ecommerce.user.service;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * This service is responsible for loading user-specific data for Spring Security.
 * It implements the UserDetailsService interface, which is used by Spring Security to handle user authentication.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final String USER_NOT_FOUND = "User not found";
    private static final String ROLE_PREFIX = "ROLE_";

    private final UserRepository userRepository;

    /**
     * Locates the user based on the username. In this implementation, the username is the user's email address.
     *
     * @param username the username (email address) identifying the user whose data is required.
     * @return a UserDetails object containing the user's credentials and other information.
     * @throws UsernameNotFoundException if the user could not be found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        // Create a collection of GrantedAuthority from the user's role
        Collection<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole())
        );

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            authorities // Pass the authorities here
        );
    }
}
