package com.iesjc.keymaster.controller;

import com.iesjc.keymaster.dto.request.PrestamoCreateDTO;
import com.iesjc.keymaster.service.PrestamoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier cliente (JavaFX, Postman, Angular...)
public class PrestamoController {

    private final PrestamoService prestamoService;

    /**
     * ENDPOINT 1: Registrar un nuevo préstamo
     * URL: POST http://localhost:8080/api/prestamos
     */
    @PostMapping
    public ResponseEntity<?> registrarPrestamo(
            @Valid @RequestBody PrestamoCreateDTO request,
            Authentication authentication) {

        // 1. Extraemos el username del usuario logueado directamente del Token JWT
        String usernameConserje = authentication.getName();

        // 2. Pasamos el mando a la capa de lógica de negocio (Service)
        Object prestamoGuardado = prestamoService.registrarNuevoPrestamo(request, usernameConserje);

        // 3. Devolvemos un código 201 (Created) indicando que el registro fue exitoso
        return new ResponseEntity<>(prestamoGuardado, HttpStatus.CREATED);
    }

    /**
     * ENDPOINT 2: Obtener préstamos activos (Para la pantalla "Active Loans")
     * URL: GET http://localhost:8080/api/prestamos/activos
     * (Estructura preparada para cuando implementemos este método en el Service)
     */
    @GetMapping("/activos")
    public ResponseEntity<?> getPrestamosActivos() {
        // Aquí llamaremos a prestamoService.obtenerActivos()
        return ResponseEntity.ok("Endpoint en construcción para listar activos");
    }

    /**
     * ENDPOINT 3: Registrar Devolución (Modal de Devolución)
     * URL: PUT http://localhost:8080/api/prestamos/{id}/devolver
     */
    @PutMapping("/{idPrestamo}/devolver")
    public ResponseEntity<?> devolverLlave(
            @PathVariable Integer idPrestamo,
            Authentication authentication) {

        String usernameConserje = authentication.getName();
        // Aquí llamaremos a prestamoService.registrarDevolucion(idPrestamo, usernameConserje)
        return ResponseEntity.ok("Endpoint en construcción para devolver la llave: " + idPrestamo);
    }
}