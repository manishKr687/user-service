package com.ecommerce.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.admin")
public class AdminUserProperties {
    private String email;
    private String password;
    private String fullName;
}
