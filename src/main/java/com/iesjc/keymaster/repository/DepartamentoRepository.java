package com.iesjc.keymaster.repository;

import com.iesjc.keymaster.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio de Departamento
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
    Optional<Departamento> findByNombre(String nombre);
}
