package com.iesjc.keymaster.service.impl;

import com.iesjc.keymaster.dto.request.PrestamoCreateDTO;
import com.iesjc.keymaster.entity.*;
import com.iesjc.keymaster.exception.ResourceNotFoundException;
import com.iesjc.keymaster.repository.*;
import com.iesjc.keymaster.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LlaveRepository llaveRepository;
    private final ProfesorRepository profesorRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional // ¡VITAL! Garantiza la atomicidad y el bloqueo optimista
    public Object registrarNuevoPrestamo(PrestamoCreateDTO request, String usernameConserje) {

        // 1. Validar al Usuario (Conserje) que hace la operación
        Usuario conserje = usuarioRepository.findByUsernameAndActivoTrue(usernameConserje)
                .orElseThrow(() -> new ResourceNotFoundException("Conserje no válido o sesión expirada"));

        // 2. Validar el Profesor (No podemos prestar a un docente dado de baja)
        Profesor profesor = profesorRepository.findById(request.getIdProfesor())
                .orElseThrow(() -> new ResourceNotFoundException("El profesor seleccionado no existe"));

        if (!profesor.getActivo()) {
            throw new IllegalStateException("El profesor está dado de baja en el sistema");
        }

        // 3. Validar la Llave (El núcleo de tu inventario)
        Llave llave = llaveRepository.findById(request.getIdLlave())
                .orElseThrow(() -> new ResourceNotFoundException("La llave seleccionada no existe"));

        if (llave.getEstado() != EstadoLlave.DISPONIBLE) {
            throw new IllegalStateException("La llave no está disponible. Estado actual: " + llave.getEstado());
        }

        // 4. Validar "Viaje en el tiempo" (Vulnerabilidad 4 de tu documento de diseño)
        if (request.getFechaLimite() != null) {
            if (request.getFechaLimite().isBefore(LocalTime.now())) {
                throw new IllegalArgumentException("La hora límite no puede ser anterior a la hora actual del sistema");
            }
        }

        // 5. Actualizar la Llave (¡Aquí se aplica el Optimistic Locking!)
        llave.setEstado(EstadoLlave.EN_USO);
        llaveRepository.save(llave);

        // 6. Registrar la transacción (El Préstamo)
        Prestamo prestamo = Prestamo.builder()
                .llave(llave)
                .profesor(profesor)
                .usuarioSalida(conserje)
                .fechaLimite(request.getFechaLimite())
                // .fechaSalida(LocalDateTime.now()) --> No hace falta, lo hace el @PrePersist automáticamente
                .build();

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);

        // Por ahora devolvemos el objeto guardado (luego lo mapearemos a un ResponseDTO)
        return prestamoGuardado;
    }
}