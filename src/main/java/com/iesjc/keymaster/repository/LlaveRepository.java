package com.iesjc.keymaster.repository;

import com.iesjc.keymaster.entity.EstadoLlave;
import com.iesjc.keymaster.entity.Llave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Repositorio de Llave
@Repository
public interface LlaveRepository extends JpaRepository<Llave, Integer> {

    Optional<Llave> findByCodigoInterno(String codigoInterno);

    // Metod0 para el Modal de "Nuevo Préstamo"
    // Solo devuelve las llaves que podamos prestar.
    List<Llave> findByEstado(EstadoLlave estado);
}