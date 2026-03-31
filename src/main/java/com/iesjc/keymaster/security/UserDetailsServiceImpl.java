package com.iesjc.keymaster.security;

import com.iesjc.keymaster.entity.Usuario;
import com.iesjc.keymaster.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscamos al usuario usando el métod0 de tu repositorio que ya creamos
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o dado de baja: " + username));

        // 2. Traducimos nuestra Entidad 'Usuario' al objeto 'User' que entiende Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword()) // Aquí va el hash BCrypt
                .roles(usuario.getRol().name())  // Spring le añadirá internamente el prefijo "ROLE_" (ej. ROLE_CONSERJE)
                .build();
    }
}