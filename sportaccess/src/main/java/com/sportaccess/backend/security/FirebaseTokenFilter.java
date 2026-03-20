package com.sportaccess.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.sportaccess.backend.model.User;
import com.sportaccess.backend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * Filtro JWT que valida el token Firebase en cada petición.
 * - Token válido: sincroniza el usuario en BD y establece el contexto de seguridad.
 * - Token inválido/expirado: responde 401 con cuerpo JSON descriptivo.
 * - Sin token: deja pasar al siguiente filtro (rutas públicas permitidas por SecurityConfig).
 * Responsable: Miembro 1 – T-02
 */
public class FirebaseTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(FirebaseTokenFilter.class);

    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FirebaseTokenFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            User user = userService.syncUser(decodedToken);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (FirebaseAuthException e) {
            log.warn("Token Firebase inválido o expirado: {}", e.getMessage());
            sendUnauthorizedResponse(response, request, "Token inválido o expirado");
        } catch (Exception e) {
            log.error("Error inesperado al validar el token Firebase: {}", e.getMessage(), e);
            sendUnauthorizedResponse(response, request, "Error de autenticación");
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response,
                                          HttpServletRequest request,
                                          String message) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorBody = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", HttpStatus.UNAUTHORIZED.value(),
                "error", "Unauthorized",
                "message", message,
                "path", request.getRequestURI()
        );
        objectMapper.writeValue(response.getWriter(), errorBody);
    }
}
