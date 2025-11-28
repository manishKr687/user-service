# RateLimitingInterceptor.java Documentation

## Overview

**File:** `RateLimitingInterceptor.java`

**Location:** `c:\F Drive\Spring\ecommerce-website\user-service\src\main\java\com\ecommerce\user\interceptor\`

**Description:**
`RateLimitingInterceptor` is a Spring MVC `HandlerInterceptor` that applies rate limiting to incoming requests based on the client's IP address. It uses the Bucket4j library to manage token buckets for each IP, preventing abuse and ensuring fair usage of the service.

**Purpose:**
The primary purpose of this interceptor is to protect the `user-service` from being overwhelmed by too many requests from a single client (identified by IP address). It ensures that API endpoints are not abused, thereby maintaining service availability and stability. When a client exceeds the configured rate limit, a `RateLimitExceededException` is thrown, leading to an HTTP 429 (Too Many Requests) response.

## Class: `RateLimitingInterceptor`

### Annotations

*   `@Component`: Indicates that this class is a Spring component, allowing it to be automatically detected and configured by Spring's component scanning.

### Implements

*   `HandlerInterceptor`: This interface from Spring MVC allows for pre-processing, post-processing, and completion-processing of requests, providing hooks before the actual handler execution, after the handler execution, and after view rendering. `RateLimitingInterceptor` specifically uses the `preHandle` method for rate limiting.

### Fields

*   `private final ProxyManager<String> proxyManager;
    *   **Type:** `io.github.bucket4j.distributed.proxy.ProxyManager<String>`
    *   **Description:** An instance of `ProxyManager` from the Bucket4j library. It is used to manage and persist `Bucket` instances, typically in a distributed cache like Redis, ensuring that rate limits are applied consistently across multiple service instances. The `<String>` generic type indicates that the key for each bucket is a `String` (in this case, the client's IP address).
*   `private final RateLimitConfig rateLimitConfig;
    *   **Type:** `com.ecommerce.user.config.RateLimitConfig`
    *   **Description:** An instance of `RateLimitConfig` which provides the necessary parameters for configuring the rate limit, such as the `capacity` (maximum tokens in the bucket) and `refillMinutes` (the duration after which the bucket tokens are refilled).

### Constructor

*   `public RateLimitingInterceptor(ProxyManager<String> proxyManager, RateLimitConfig rateLimitConfig)`
    *   **Description:** Initializes the `RateLimitingInterceptor` with the required `ProxyManager` and `RateLimitConfig` instances. These are typically injected by Spring's dependency injection mechanism.
    *   **Parameters:**
        *   `proxyManager`: The `ProxyManager` used to manage and persist `Bucket` instances.
        *   `rateLimitConfig`: The `RateLimitConfig` providing rate limiting parameters.

### Methods

#### `preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)`

*   **Description:** This method is called before the actual handler (controller method) is executed. It implements the core rate-limiting logic.
*   **Parameters:**
    *   `request`: The current `HttpServletRequest`, used to extract the client's IP address and potentially add headers.
    *   `response`: The current `HttpServletResponse`, used to add the `X-Rate-Limit-Remaining` header.
    *   `handler`: The handler (or `Object` handler) that the request was mapped to.
*   **Returns:** `boolean`
    *   `true` if the request is allowed to proceed to the controller.
    *   `false` if the request is blocked (though an exception is thrown instead, which typically prevents further processing).
*   **Throws:**
    *   `Exception`: If an error occurs during interception.
    *   `RateLimitExceededException`: If the client has exceeded the defined rate limit. This exception will be caught by `GlobalExceptionHandler` to return an HTTP 429 response.
*   **Logic:**
    1.  **Extract IP Address:** Calls `getClientIp(request)` to determine the client's IP address.
    2.  **Get/Create Bucket:** Uses `proxyManager.builder().build(ipAddress, this::createNewBucket)` to retrieve the `Bucket` associated with the IP address. If no bucket exists for that IP, `createNewBucket()` is invoked to create and configure a new one.
    3.  **Consume Token:** Attempts to consume one token from the bucket using `bucket.tryConsumeAndReturnRemaining(1)`.
    4.  **Check Consumption Result:**
        *   If `probe.isConsumed()` is `true`: The token was successfully consumed.
            *   Adds an `X-Rate-Limit-Remaining` header to the response, indicating how many tokens are left for the client.
            *   Returns `true` to allow the request to proceed.
        *   If `probe.isConsumed()` is `false`: The token could not be consumed (rate limit exceeded).
            *   Calculates `waitForRefillSeconds`, the time in seconds until the next token is available.
            *   Throws a `RateLimitExceededException` with a message "Too many requests" and the `waitForRefillSeconds`.

#### `createNewBucket()`

*   **Description:** This private helper method is responsible for defining the configuration for a new rate-limiting bucket. It is called by the `ProxyManager` when a bucket needs to be created for a new or unrecognized IP address.
*   **Returns:** `BucketConfiguration`
    *   A configured `BucketConfiguration` instance specifying the rate-limiting rules.
*   **Logic:**
    1.  Uses `BucketConfiguration.builder()` to start building the configuration.
    2.  Adds a limit using `addLimit()`.
    3.  Configures a `Bandwidth.classic()` with the `capacity` obtained from `rateLimitConfig.getCapacity()`.
    4.  Defines the `Refill.intervally()` strategy:
        *   Refills `rateLimitConfig.getCapacity()` tokens.
        *   The refill happens every `rateLimitConfig.getRefillMinutes()` minutes (converted to `Duration`).
    5.  Builds and returns the `BucketConfiguration`.

#### `getClientIp(HttpServletRequest request)`

*   **Description:** Extracts the client's IP address from the `HttpServletRequest`. It handles requests that might come through a proxy by checking the "X-Forwarded-For" header.
*   **Parameters:**
    *   `request`: The `HttpServletRequest` from which to extract the IP.
*   **Returns:** `String`
    *   The IP address of the client.
*   **Logic:**
    1.  Retrieves the "X-Forwarded-For" header from the request.
    2.  If the header is null or blank, it falls back to `request.getRemoteAddr()`, which gets the direct IP address of the client (or the last proxy).
    3.  If "X-Forwarded-For" header is present, it splits the header value by comma (as it can contain a list of IPs) and takes the first IP in the list, trimming any whitespace. This is a common pattern for identifying the original client IP when behind a proxy.

## Error Handling

*   **`RateLimitExceededException`**: This custom exception is thrown by `preHandle` when a client's request rate surpasses the configured limit. This exception is designed to be caught by the application's global exception handler (`GlobalExceptionHandler`) which then translates it into an appropriate HTTP 429 (Too Many Requests) response, often including a "Retry-After" header derived from the `waitForRefillSeconds`.

## Integration

This interceptor needs to be registered with Spring MVC to be active. This is typically done in a `WebConfig` class by overriding the `addInterceptors` method.

```