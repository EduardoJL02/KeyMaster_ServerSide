package com.iesjc.keymaster.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extraemos la cabecera "Authorization" de la petición HTTP
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Si no hay cabecera o no empieza por "Bearer ", ignoramos y pasamos al siguiente filtro
        // (Spring Security bloqueará la petición más adelante devolviendo un 403/401)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraemos el token (quitando los primeros 7 caracteres: "Bearer ")
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // 4. Si hay usuario en el token y NO está autenticado todavía en este hilo de ejecución...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargamos los datos del usuario desde MySQL
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. Verificamos que el token no esté caducado y pertenezca a este usuario
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Creamos el "Pase VIP" oficial de Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Añadimos detalles de la petición (IP, sesión, etc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Guardamos el usuario en el contexto de seguridad. ¡A partir de aquí, está logueado!
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Continuamos con la cadena de ejecución (dejar pasar la petición al Controller)
        filterChain.doFilter(request, response);
    }
}