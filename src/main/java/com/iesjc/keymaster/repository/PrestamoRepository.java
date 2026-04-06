package com.iesjc.keymaster.repository;

import com.iesjc.keymaster.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

//Repositorio de Prestamo
@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    // Si la fechaEntrada es NULL, la llave está fuera.
    List<Prestamo> findByFechaEntradaIsNull();

    // Para saber si un profesor en concreto tiene llaves sin devolver
    List<Prestamo> findByProfesor_IdProfesorAndFechaEntradaIsNull(Integer idProfesor);

    // Consulta personalizada (JPQL) para los Reportes PDF
    // Devuelve el historial de una llave ordenado desde el más reciente
    @Query("SELECT p FROM Prestamo p WHERE p.llave.idLlave = :idLlave ORDER BY p.fechaSalida DESC")
    List<Prestamo> findHistorialByLlave(@Param("idLlave") Integer idLlave);

    // Para la tabla "Actividad Reciente" del Dashboard
    List<Prestamo> findTop20ByOrderByFechaSalidaDesc();

    // Busca los préstamos cuya fecha de salida esté entre dos fechas dadas
    List<Prestamo> findByFechaSalidaBetweenOrderByFechaSalidaDesc(LocalDateTime inicio, LocalDateTime fin);
}