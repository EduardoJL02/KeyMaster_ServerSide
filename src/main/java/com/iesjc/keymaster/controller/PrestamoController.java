package com.iesjc.keymaster.controller;

import com.iesjc.keymaster.dto.request.PrestamoCreateDTO;
import com.iesjc.keymaster.dto.response.PrestamoResponseDTO;
import com.iesjc.keymaster.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PrestamoController {

    private final PrestamoService prestamoService;

    /**
     * ENDPOINT 1: Registrar un nuevo préstamo
     * URL: POST http://localhost:8080/api/prestamos
     */
    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> registrarPrestamo(
            @Valid @RequestBody PrestamoCreateDTO request,
            Authentication authentication) {
        PrestamoResponseDTO response = prestamoService.registrarNuevoPrestamo(request, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * ENDPOINT 2: Obtener préstamos activos (Para la pantalla "Active Loans")
     * URL: GET http://localhost:8080/api/prestamos/activos
     * (Estructura preparada para cuando implementemos este métod0 en el Service)
     */
    @GetMapping("/activos")
    public ResponseEntity<List<PrestamoResponseDTO>> getPrestamosActivos() {
        List<PrestamoResponseDTO> activos = prestamoService.obtenerActivos();
        return ResponseEntity.ok(activos);
    }

    /**
     * ENDPOINT 3: Registrar Devolución (Modal de Devolución)
     * URL: PUT http://localhost:8080/api/prestamos/{id}/devolver
     */
    @PutMapping("/{idPrestamo}/devolver")
    public ResponseEntity<PrestamoResponseDTO> devolverLlave(
            @PathVariable Integer idPrestamo,
            Authentication authentication) {
        PrestamoResponseDTO response = prestamoService.registrarDevolucion(idPrestamo, authentication.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * ENDPOINT 4: Obtener Actividad Reciente (Para la tabla del Dashboard)
     * URL: GET http://localhost:8080/api/prestamos/recientes
     */
    @GetMapping("/recientes")
    public ResponseEntity<List<PrestamoResponseDTO>> getActividadReciente() {
        List<PrestamoResponseDTO> recientes = prestamoService.obtenerActividadReciente();
        return ResponseEntity.ok(recientes);
    }

    /**
     * ENDPOINT 5: Obtener Detalles de un Préstamo (Para el botón "Ojo" de Detalles)
     * URL: GET http://localhost:8080/api/prestamos/{idPrestamo}
     */
    @GetMapping("/{idPrestamo}")
    public ResponseEntity<PrestamoResponseDTO> getPrestamoPorId(@PathVariable Integer idPrestamo) {
        PrestamoResponseDTO response = prestamoService.obtenerPrestamoPorId(idPrestamo);
        return ResponseEntity.ok(response);
    }
}