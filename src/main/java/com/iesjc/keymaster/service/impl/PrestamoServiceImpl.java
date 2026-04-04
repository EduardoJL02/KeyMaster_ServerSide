package com.iesjc.keymaster.service.impl;

import com.iesjc.keymaster.dto.request.PrestamoCreateDTO;
import com.iesjc.keymaster.dto.response.PrestamoResponseDTO;
import com.iesjc.keymaster.entity.*;
import com.iesjc.keymaster.exception.ResourceNotFoundException;
import com.iesjc.keymaster.repository.*;
import com.iesjc.keymaster.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LlaveRepository llaveRepository;
    private final ProfesorRepository profesorRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public PrestamoResponseDTO registrarNuevoPrestamo(PrestamoCreateDTO request, String usernameConserje) {
        Usuario conserje = usuarioRepository.findByUsernameAndActivoTrue(usernameConserje)
                .orElseThrow(() -> new IllegalArgumentException("Conserje no válido"));

        Profesor profesor = profesorRepository.findById(request.getIdProfesor())
                .orElseThrow(() -> new ResourceNotFoundException("El profesor seleccionado no existe"));

        if (!profesor.getActivo()) throw new IllegalStateException("El profesor está dado de baja");

        Llave llave = llaveRepository.findById(request.getIdLlave())
                .orElseThrow(() -> new ResourceNotFoundException("La llave no existe"));

        if (llave.getEstado() != EstadoLlave.DISPONIBLE) {
            throw new IllegalStateException("La llave no está disponible.");
        }

        if (request.getFechaLimite() != null && request.getFechaLimite().isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("La hora límite no puede ser en el pasado.");
        }

        llave.setEstado(EstadoLlave.EN_USO);
        llaveRepository.save(llave);

        Prestamo prestamo = Prestamo.builder()
                .llave(llave)
                .profesor(profesor)
                .usuarioSalida(conserje)
                .fechaLimite(request.getFechaLimite())
                .build();

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);

        // Devolvemos el DTO
        return mapToDTO(prestamoGuardado);
    }

    @Override
    @Transactional(readOnly = true) // Optimiza la consulta en BD
    public List<PrestamoResponseDTO> obtenerActivos() {
        // Usamos el métod0 de tu Repository que busca los que no tienen fecha de entrada
        return prestamoRepository.findByFechaEntradaIsNull().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PrestamoResponseDTO registrarDevolucion(Integer idPrestamo, String usernameConserje) {

        // 1. Buscar el préstamo y el conserje que registra la entrada
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + idPrestamo));

        Usuario conserjeEntrada = usuarioRepository.findByUsernameAndActivoTrue(usernameConserje)
                .orElseThrow(() -> new IllegalArgumentException("Conserje no válido"));

        // 2. Validar que no esté ya devuelto
        if (prestamo.getFechaEntrada() != null) {
            throw new IllegalStateException("Este préstamo ya fue devuelto anteriormente.");
        }

        // 3. Registrar los datos de la devolución
        prestamo.setFechaEntrada(LocalDateTime.now());
        prestamo.setUsuarioEntrada(conserjeEntrada);

        // 4. Liberar la llave (Optimistic Locking actuará aquí también)
        Llave llave = prestamo.getLlave();
        llave.setEstado(EstadoLlave.DISPONIBLE);
        llaveRepository.save(llave);

        // 5. Guardar y mapear
        Prestamo prestamoActualizado = prestamoRepository.save(prestamo);
        return mapToDTO(prestamoActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> obtenerActividadReciente() {
        return prestamoRepository.findTop20ByOrderByFechaSalidaDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponseDTO obtenerPrestamoPorId(Integer idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + idPrestamo));
        return mapToDTO(prestamo);
    }

    // --- MÉTOD0 PRIVADO DE MAPEO E INTELIGENCIA ---
    private PrestamoResponseDTO mapToDTO(Prestamo prestamo) {
        String estadoCalculado = "DEVUELTO";

        // Inteligencia de negocio para el estado dinámico
        if (prestamo.getFechaEntrada() == null) {
            if (prestamo.getFechaLimite() != null && LocalTime.now().isAfter(prestamo.getFechaLimite())) {
                estadoCalculado = "VENCIDO";
            } else {
                estadoCalculado = "A TIEMPO";
            }
        }

        return PrestamoResponseDTO.builder()
                .idPrestamo(prestamo.getIdPrestamo())
                .codigoLlave(prestamo.getLlave().getCodigoInterno())
                .espacio(prestamo.getLlave().getEspacio().getCodigo())
                .nombreProfesor(prestamo.getProfesor().getNombre() + " " + prestamo.getProfesor().getApellidos())
                .dniProfesor(prestamo.getProfesor().getDni())
                .fechaSalida(prestamo.getFechaSalida())
                .fechaLimite(prestamo.getFechaLimite())
                .fechaEntrada(prestamo.getFechaEntrada())
                .estado(estadoCalculado)
                .build();
    }
}