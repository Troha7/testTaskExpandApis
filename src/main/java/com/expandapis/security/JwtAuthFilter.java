package com.expandapis.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.expandapis.exception.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * {@link JwtAuthFilter}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final HandlerExceptionResolver handlerExceptionResolver;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    if (isNotAuthorizationHeader(request)) {
      logger.warn("Header does not contain " + AUTHORIZATION);
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String jwtToken = jwtService.extractToken(request);
      String email = jwtService.extractEmail(jwtToken);

      if (email != null && getAuthentication() == null) {
        var userDetails = userDetailsService.loadUserByUsername(email);

        if (isTokenValid(jwtToken, userDetails)) {
          setAuthentication(userDetails, request);
        }
      }

      filterChain.doFilter(request, response);

    } catch (JwtException | InvalidTokenException e) {
      handlerExceptionResolver.resolveException(request, response, null, e);
    }
  }

  private Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  private Boolean isTokenValid(String jwtToken, UserDetails userDetails) {
    return jwtService.isTokenValid(jwtToken, userDetails);
  }

  private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
    var authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  private boolean isNotAuthorizationHeader(HttpServletRequest request) {
    return request.getHeader(AUTHORIZATION) == null;
  }

}
