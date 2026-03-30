package com.iesjc.keymaster.repository;

import com.iesjc.keymaster.entity.ConfiguracionGlobal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionGlobalRepository extends JpaRepository<ConfiguracionGlobal, Integer> {

    // Métod0 para buscar una configuración específica (ej. "SMTP_HOST")
    Optional<ConfiguracionGlobal> findByClave(String clave);
}