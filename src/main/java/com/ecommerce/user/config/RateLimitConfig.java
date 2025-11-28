package com.ecommerce.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class holds the configuration properties for the rate limiting feature.
 * It is a type-safe representation of the properties defined in {@code application.yml} with the prefix "rate.limit".
 */
@Data
@Component
@ConfigurationProperties(prefix = "rate.limit")
public class RateLimitConfig {

    /**
     * The maximum number of requests allowed in a given time window (the bucket capacity).
     */
    private int capacity;

    /**
     * The time it takes to refill the bucket with new tokens, in minutes.
     */
    private int refillMinutes;
}
