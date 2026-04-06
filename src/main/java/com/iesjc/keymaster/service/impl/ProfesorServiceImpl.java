package com.iesjc.keymaster.service.impl;

import com.iesjc.keymaster.dto.request.ProfesorRequestDTO;
import com.iesjc.keymaster.dto.response.ProfesorResponseDTO;
import com.iesjc.keymaster.entity.Departamento;
import com.iesjc.keymaster.entity.Prestamo;
import com.iesjc.keymaster.entity.Profesor;
import com.iesjc.keymaster.exception.ResourceNotFoundException;
import com.iesjc.keymaster.repository.DepartamentoRepository;
import com.iesjc.keymaster.repository.PrestamoRepository;
import com.iesjc.keymaster.repository.ProfesorRepository;
import com.iesjc.keymaster.service.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final DepartamentoRepository departamentoRepository;
    private final PrestamoRepository prestamoRepository; // Necesario para comprobar préstamos activos

    @Override
    @Transactional(readOnly = true)
    public List<ProfesorResponseDTO> obtenerTodos(boolean incluirBajas) {
        List<Profesor> profesores = incluirBajas ?
                profesorRepository.findAll() :
                profesorRepository.findByActivoTrue();

        return profesores.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProfesorResponseDTO obtenerPorId(Integer idProfesor) {
        Profesor profesor = profesorRepository.findById(idProfesor)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con ID: " + idProfesor));
        return mapToDTO(profesor);
    }

    @Override
    @Transactional
    public ProfesorResponseDTO crearProfesor(ProfesorRequestDTO request) {
        // 1. Validación Matemática del DNI (Vulnerabilidad 2 del PDF)
        if (!esDniValido(request.getDni())) {
            throw new IllegalArgumentException("La letra del DNI no se corresponde con la numeración oficial.");
        }

        // 2. Validar que el DNI no exista ya en la BD
        Optional<Profesor> existente = profesorRepository.findByDni(request.getDni().toUpperCase());
        if (existente.isPresent()) {
            throw new DataIntegrityViolationException("El DNI '" + request.getDni() + "' ya está registrado.");
        }

        // 3. Asociar Departamento
        Departamento departamento = departamentoRepository.findById(request.getIdDepartamento())
                .orElseThrow(() -> new ResourceNotFoundException("El departamento seleccionado no existe"));

        // 4. Guardar (Se crea Activo por defecto)
        Profesor nuevoProfesor = Profesor.builder()
                .dni(request.getDni().toUpperCase())
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .departamento(departamento)
                .activo(true)
                .build();

        Profesor profesorGuardado = profesorRepository.save(nuevoProfesor);
        return mapToDTO(profesorGuardado);
    }

    @Override
    @Transactional
    public ProfesorResponseDTO actualizarProfesor(Integer idProfesor, ProfesorRequestDTO request) {
        Profesor profesor = profesorRepository.findById(idProfesor)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        if (!profesor.getDni().equalsIgnoreCase(request.getDni())) {
            if (!esDniValido(request.getDni())) {
                throw new IllegalArgumentException("La letra del nuevo DNI no es válida.");
            }
            if (profesorRepository.findByDni(request.getDni().toUpperCase()).isPresent()) {
                throw new DataIntegrityViolationException("El nuevo DNI ya está en uso.");
            }
            profesor.setDni(request.getDni().toUpperCase());
        }

        Departamento departamento = departamentoRepository.findById(request.getIdDepartamento())
                .orElseThrow(() -> new ResourceNotFoundException("El departamento no existe"));

        profesor.setNombre(request.getNombre());
        profesor.setApellidos(request.getApellidos());
        profesor.setEmail(request.getEmail());
        profesor.setDepartamento(departamento);

        return mapToDTO(profesorRepository.save(profesor));
    }

    @Override
    @Transactional
    public void darDeBaja(Integer idProfesor) {
        Profesor profesor = profesorRepository.findById(idProfesor)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        // Validación crítica: ¿Tiene llaves sin devolver? (Vulnerabilidad del PDF)
        List<Prestamo> prestamosActivos = prestamoRepository.findByProfesor_IdProfesorAndFechaEntradaIsNull(idProfesor);
        if (!prestamosActivos.isEmpty()) {
            throw new IllegalStateException("No se puede dar de baja a este docente porque tiene " + prestamosActivos.size() + " llave(s) sin devolver.");
        }

        // Soft Delete
        profesor.setActivo(false);
        profesorRepository.save(profesor);
    }

    // --- ALGORITMO OFICIAL DE VALIDACIÓN DE DNI ESPAÑOL ---
    private boolean esDniValido(String dni) {
        if (dni == null || !dni.matches("^[0-9]{8}[A-Za-z]$")) {
            return false;
        }
        String numeros = dni.substring(0, 8);
        char letra = Character.toUpperCase(dni.charAt(8));
        String letrasValidas = "TRWAGMYFPDXBNJZSQVHLCKE";
        int modulo = Integer.parseInt(numeros) % 23;
        return letrasValidas.charAt(modulo) == letra;
    }

    // --- MAPEO DTO ---
    private ProfesorResponseDTO mapToDTO(Profesor profesor) {
        return ProfesorResponseDTO.builder()
                .idProfesor(profesor.getIdProfesor())
                .dni(profesor.getDni())
                .nombre(profesor.getNombre())
                .apellidos(profesor.getApellidos())
                .email(profesor.getEmail())
                .nombreDepartamento(profesor.getDepartamento().getNombre())
                .activo(profesor.getActivo())
                .build();
    }
}