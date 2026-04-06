package com.iesjc.keymaster.controller;

import com.iesjc.keymaster.dto.request.AjustesGlobalesDTO;
import com.iesjc.keymaster.service.ConfiguracionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    // Cualquier usuario logueado (Conserje o Jefatura) puede VER la configuración
    @GetMapping
    public ResponseEntity<AjustesGlobalesDTO> getAjustes() {
        return ResponseEntity.ok(configuracionService.obtenerAjustes());
    }

    // SOLO JEFATURA puede modificar la configuración (Falsa Seguridad prevenida)
    @PutMapping
    @PreAuthorize("hasRole('JEFATURA')")
    public ResponseEntity<AjustesGlobalesDTO> updateAjustes(@RequestBody AjustesGlobalesDTO request) {
        return ResponseEntity.ok(configuracionService.guardarAjustes(request));
    }

    // SOLO JEFATURA puede extraer una copia de seguridad
    @PostMapping("/backup")
    @PreAuthorize("hasRole('JEFATURA')")
    public ResponseEntity<byte[]> generarBackup() {
        byte[] backupBytes = configuracionService.generarBackupBaseDatos();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "KeyMaster_Backup.sql");

        return new ResponseEntity<>(backupBytes, headers, HttpStatus.OK);
    }
}