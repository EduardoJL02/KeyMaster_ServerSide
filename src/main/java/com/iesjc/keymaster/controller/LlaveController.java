package com.iesjc.keymaster.controller;

import com.iesjc.keymaster.dto.request.LlaveRequestDTO;
import com.iesjc.keymaster.dto.response.LlaveResponseDTO;
import com.iesjc.keymaster.service.LlaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/llaves")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permite la comunicación con el cliente JavaFX
public class LlaveController {

    private final LlaveService llaveService;

    /**
     * ENDPOINT 1: Obtener todas las llaves (Para llenar la tabla inicial)
     * URL: GET http://localhost:8080/api/llaves
     */
    @GetMapping
    public ResponseEntity<List<LlaveResponseDTO>> getAllLlaves() {
        List<LlaveResponseDTO> llaves = llaveService.obtenerTodas();
        return ResponseEntity.ok(llaves);
    }

    /**
     * ENDPOINT 2: Obtener una llave específica (Por si lo necesitamos para detalles)
     * URL: GET http://localhost:8080/api/llaves/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<LlaveResponseDTO> getLlaveById(@PathVariable Integer id) {
        LlaveResponseDTO llave = llaveService.obtenerPorId(id);
        return ResponseEntity.ok(llave);
    }

    /**
     * ENDPOINT 3: Crear una nueva llave (Para el modal "AÑADIR NUEVA LLAVE")
     * URL: POST http://localhost:8080/api/llaves
     */
    @PostMapping
    public ResponseEntity<LlaveResponseDTO> createLlave(@Valid @RequestBody LlaveRequestDTO request) {
        LlaveResponseDTO nuevaLlave = llaveService.crearLlave(request);
        // Devolvemos 201 Created como especificas en el PDF
        return new ResponseEntity<>(nuevaLlave, HttpStatus.CREATED);
    }

    /**
     * ENDPOINT 4: Editar una llave existente (Para el botón "Lápiz" de la tabla)
     * URL: PUT http://localhost:8080/api/llaves/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<LlaveResponseDTO> updateLlave(
            @PathVariable Integer id,
            @Valid @RequestBody LlaveRequestDTO request) {
        LlaveResponseDTO llaveActualizada = llaveService.actualizarLlave(id, request);
        return ResponseEntity.ok(llaveActualizada);
    }

    /**
     * ENDPOINT 5: Eliminar una llave (Para el botón "Papelera" de la tabla)
     * URL: DELETE http://localhost:8080/api/llaves/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLlave(@PathVariable Integer id) {
        llaveService.eliminarLlave(id);
        // Devolvemos 204 No Content (Éxito, pero sin cuerpo JSON de respuesta)
        return ResponseEntity.noContent().build();
    }
}