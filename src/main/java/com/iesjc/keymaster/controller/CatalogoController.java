package com.iesjc.keymaster.controller;

import com.iesjc.keymaster.dto.response.DepartamentoResponseDTO;
import com.iesjc.keymaster.dto.response.EspacioResponseDTO;
import com.iesjc.keymaster.service.DepartamentoService;
import com.iesjc.keymaster.service.EspacioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CatalogoController {

    private final DepartamentoService departamentoService;
    private final EspacioService espacioService;

    // URL: GET http://localhost:8080/api/catalogos/departamentos
    @GetMapping("/departamentos")
    public ResponseEntity<List<DepartamentoResponseDTO>> getDepartamentos() {
        return ResponseEntity.ok(departamentoService.obtenerTodos());
    }

    // URL: GET http://localhost:8080/api/catalogos/espacios
    @GetMapping("/espacios")
    public ResponseEntity<List<EspacioResponseDTO>> getEspacios() {
        return ResponseEntity.ok(espacioService.obtenerTodos());
    }
}