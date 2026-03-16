package com.iesjc.keymaster.repository;

import com.iesjc.keymaster.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio de Usuario
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Metod0 que usará Spring Security para buscar al usuario al hacer login
    Optional<Usuario> findByUsernameAndActivoTrue(String username);
}
