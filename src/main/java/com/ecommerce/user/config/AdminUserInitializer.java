package com.ecommerce.user.config;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminUserProperties adminUserProperties;

    private static final String ADMIN_ROLE = "ADMIN";

    @Override
    public void run(String... args) {
        if (userRepository.existsByRole(ADMIN_ROLE)) {
            log.info("Admin user already exists. Skipping creation.");
            return;
        }

        if (adminUserProperties.getEmail() == null || adminUserProperties.getPassword() == null) {
            log.warn("Admin user credentials are not configured. Skipping creation.");
            return;
        }
        
        User adminUser = new User();
        adminUser.setFullName(adminUserProperties.getFullName());
        adminUser.setEmail(adminUserProperties.getEmail());
        adminUser.setPassword(passwordEncoder.encode(adminUserProperties.getPassword()));
        adminUser.setRole(ADMIN_ROLE);

        userRepository.save(adminUser);
        log.info("Admin user created successfully.");
    }
}
