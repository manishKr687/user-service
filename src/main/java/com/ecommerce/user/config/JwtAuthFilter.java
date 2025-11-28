
package com.ecommerce.user.config;

import com.ecommerce.user.service.CustomUserDetailsService;
import com.ecommerce.user.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**

 * A filter that intercepts incoming HTTP requests to authenticate users based on a JWT token.

 * This filter is executed once per request.

 */

@Component

@RequiredArgsConstructor

public class JwtAuthFilter extends OncePerRequestFilter {



    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;



    /**

     * This method is called for each incoming request. It extracts the JWT token from the "Authorization" header,

     * validates it, and sets the user's authentication in the Spring Security context if the token is valid.

     *

     * @param request     The incoming HTTP request.

     * @param response    The outgoing HTTP response.

     * @param filterChain The filter chain to pass the request and response to the next filter.

     * @throws ServletException if a servlet-specific error occurs.

     * @throws IOException      if an I/O error occurs.

     */

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        String token = null;

        String username = null;



        // Check if the request has an Authorization header with a Bearer token.

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            username = jwtService.extractUsername(token);

        }



        // If a token is found and there is no existing authentication in the security context.

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from the database.

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);



            // Validate the token.

            if (jwtService.validateToken(token, userDetails)) {

                // If the token is valid, create an authentication token and set it in the security context.

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }

        // Continue the filter chain.

        filterChain.doFilter(request, response);

    }

}
