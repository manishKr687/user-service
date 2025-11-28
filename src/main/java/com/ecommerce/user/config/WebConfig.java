
package com.ecommerce.user.config;

import com.ecommerce.user.interceptor.RateLimitingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class provides web-related configuration for the application.
 * It implements the WebMvcConfigurer interface to customize the Spring MVC setup.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RateLimitingInterceptor rateLimitingInterceptor;

    /**
     * Registers the RateLimitingInterceptor to intercept all incoming requests.
     *
     * @param registry The InterceptorRegistry to add the interceptor to.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor);
    }
}
