package com.iesjc.keymaster.controller;

import com.iesjc.keymaster.dto.request.ProfesorRequestDTO;
import com.iesjc.keymaster.dto.response.ProfesorResponseDTO;
import com.iesjc.keymaster.service.ProfesorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfesorController {

    private final ProfesorService profesorService;

    /**
     * ENDPOINT 1: Obtener todos los docentes
     * URL: GET http://localhost:8080/api/profesores?incluirBajas=true/false
     * Permite al Dashboard y a la vista de personal cargar los datos según el filtro.
     */
    @GetMapping
    public ResponseEntity<List<ProfesorResponseDTO>> getAllProfesores(
            @RequestParam(defaultValue = "false") boolean incluirBajas) {
        List<ProfesorResponseDTO> profesores = profesorService.obtenerTodos(incluirBajas);
        return ResponseEntity.ok(profesores);
    }

    /**
     * ENDPOINT 2: Obtener un docente por ID
     * URL: GET http://localhost:8080/api/profesores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfesorResponseDTO> getProfesorById(@PathVariable Integer id) {
        ProfesorResponseDTO profesor = profesorService.obtenerPorId(id);
        return ResponseEntity.ok(profesor);
    }

    /**
     * ENDPOINT 3: Crear nuevo docente (Modal "AÑADIR NUEVO DOCENTE")
     * URL: POST http://localhost:8080/api/profesores
     */
    @PostMapping
    public ResponseEntity<ProfesorResponseDTO> createProfesor(@Valid @RequestBody ProfesorRequestDTO request) {
        ProfesorResponseDTO nuevoProfesor = profesorService.crearProfesor(request);
        return new ResponseEntity<>(nuevoProfesor, HttpStatus.CREATED);
    }

    /**
     * ENDPOINT 4: Editar docente (Botón "Lápiz")
     * URL: PUT http://localhost:8080/api/profesores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfesorResponseDTO> updateProfesor(
            @PathVariable Integer id,
            @Valid @RequestBody ProfesorRequestDTO request) {
        ProfesorResponseDTO actualizado = profesorService.actualizarProfesor(id, request);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * ENDPOINT 5: Dar de baja (Botón "Papelera" -> Soft Delete)
     * URL: DELETE http://localhost:8080/api/profesores/{id}
     * Realiza un borrado lógico para preservar la integridad del historial.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesor(@PathVariable Integer id) {
        profesorService.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }
}