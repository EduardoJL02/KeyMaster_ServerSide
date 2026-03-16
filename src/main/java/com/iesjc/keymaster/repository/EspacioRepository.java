package com.iesjc.keymaster.repository;

import com.iesjc.keymaster.entity.Espacio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio de Espacio
@Repository
public interface EspacioRepository extends JpaRepository<Espacio, Integer> {
    Optional<Espacio> findByCodigo(String codigo);
}
