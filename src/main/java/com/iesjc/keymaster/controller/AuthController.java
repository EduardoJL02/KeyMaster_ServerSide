package com.iesjc.keymaster.controller;

import com.iesjc.keymaster.dto.request.LoginRequestDTO;
import com.iesjc.keymaster.dto.response.LoginResponseDTO;
import com.iesjc.keymaster.entity.Usuario;
import com.iesjc.keymaster.repository.UsuarioRepository;
// Nota: JwtService lo crearemos en el próximo paso de seguridad
// import com.iesjc.keymaster.security.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
// Habilitamos CORS temporalmente para evitar bloqueos si pruebas con Postman o un cliente externo
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    // private final JwtService jwtService; // (Pendiente de crear)

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        // 1. Spring Security intenta autenticar con el usuario y contraseña plana
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Si llegamos a esta línea, las credenciales son CORRECTAS.
        // Buscamos al usuario en la BD para obtener su Rol real.
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado tras autenticar"));

        // 3. Generamos el Token JWT (Lógica simulada por ahora hasta crear el JwtService)
        // String token = jwtService.generateToken(usuario);
        String token = "jwt_token_simulado_temporalmente";

        // 4. Construimos el DTO de respuesta que necesita tu JavaFX
        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol().name())
                .message("Autenticación exitosa")
                .build();

        return ResponseEntity.ok(response); // Devuelve un HTTP 200 OK con el JSON
    }
}