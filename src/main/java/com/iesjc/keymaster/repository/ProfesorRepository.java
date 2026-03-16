package com.iesjc.keymaster.repository;

import com.iesjc.keymaster.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio de Profesor
@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {

    Optional<Profesor> findByDni(String dni);

    // No cargar los profesores dados de baja.
    List<Profesor> findByActivoTrue();
}