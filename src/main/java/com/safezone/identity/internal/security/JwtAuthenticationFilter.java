package com.safezone.identity.internal.security;

import com.safezone.shared.web.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Validates a Bearer access token (if present) and, on success, both authenticates the
 * request (for {@code authorizeHttpRequests}/future {@code @PreAuthorize}) and wraps it so
 * downstream controllers see trustworthy X-User-Id/X-Organization-Id headers - see {@link
 * ClaimsHeaderRequestWrapper}. An invalid/expired/missing token is left unauthenticated
 * rather than rejected here; SecurityConfig's entry point turns that into a 401 only for
 * endpoints that actually require authentication (so /api/auth/** still works unauthenticated).
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                var claims = jwtService.parseAndValidate(header.substring("Bearer ".length()));
                var authorities = claims.roles().stream()
                        .<GrantedAuthority>map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .toList();
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(claims, null, authorities));
                filterChain.doFilter(new ClaimsHeaderRequestWrapper(request, claims), response);
                return;
            } catch (UnauthorizedException e) {
                // Leave unauthenticated; fall through below.
            }
        }
        filterChain.doFilter(request, response);
    }
}
