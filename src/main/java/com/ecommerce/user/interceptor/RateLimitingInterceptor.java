package com.ecommerce.user.interceptor;

import com.ecommerce.user.config.RateLimitConfig;
import io.github.bucket4j.Bucket; // Corrected import
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ecommerce.user.exception.RateLimitExceededException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Duration;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final RateLimitConfig rateLimitConfig;

    // In-memory storage of buckets per IP
    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    public RateLimitingInterceptor(RateLimitConfig rateLimitConfig) {
        this.rateLimitConfig = rateLimitConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String ipAddress = getClientIp(request);

        Bucket bucket = bucketCache.computeIfAbsent(ipAddress, key -> createNewBucket());

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader(
                    "X-Rate-Limit-Remaining",
                    String.valueOf(probe.getRemainingTokens())
            );
            return true;
        } else {
            long waitForRefillSeconds =
                    probe.getNanosToWaitForRefill() / 1_000_000_000;

            throw new RateLimitExceededException("Too many requests", waitForRefillSeconds);
        }
    }

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(
                        Bandwidth.classic(
                                rateLimitConfig.getCapacity(),
                                Refill.intervally(
                                        rateLimitConfig.getCapacity(),
                                        Duration.ofMinutes(
                                                rateLimitConfig.getRefillMinutes()
                                        )
                                )
                        )
                )
                .build();
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isBlank()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
}
