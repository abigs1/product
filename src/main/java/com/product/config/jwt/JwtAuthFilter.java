package com.product.config.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Intercepta la peticion, si trae un token lo valida y lo guarda en memoria
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Revisa si la peticion trae el header Authorization
        String authHeader = request.getHeader("Authorization");

        // Si no trae un token Bearer, deja pasar la peticion sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            Long userId = jwtUtil.extractUserId(token);
            List<String> permisosList = jwtUtil.extractPermisos(token);

            // Si el token tiene username y todavia no hay usuario autenticado
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = User.withUsername(username)
                        .password("")
                        .authorities(AuthorityUtils.createAuthorityList(permisosList.toArray(new String[0])))
                        .build();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(userId);

                // Guarda en memoria el id, username y roles durante esta peticion
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (JwtException | IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalido o mal formado");
            return;
        }

        // Pasa la peticion al siguiente filtro
        chain.doFilter(request, response);
    }
}
