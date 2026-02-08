package io.github.artsobol.shortlink.infrastructure.security.jwt;

import io.github.artsobol.shortlink.exception.http.BadRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws
            ServletException,
            IOException {

        String header = request.getHeader("Authorization");
        if (!checkHeader(header)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Claims claims = parseToken(header);
            createAuthentication(claims.getSubject(), getAuthority(claims));
            filterChain.doFilter(request, response);
        }
    }

    private boolean checkHeader(String header) {
        return header != null && header.startsWith("Bearer ");
    }

    private void createAuthentication(String username, List<SimpleGrantedAuthority> authorities) {
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private Claims parseToken(String header) {
        try {
            String token = header.substring(7);
            return jwtTokenProvider.parseToken(token);
        } catch (JwtException e) {
            throw new BadRequestException("token.invalid");
        }
    }

    private List<SimpleGrantedAuthority> getAuthority(Claims claims) {
        List<?> roles = claims.get("roles", List.class);
        return roles.stream().map(String.class::cast).map(SimpleGrantedAuthority::new).toList();
    }
}
