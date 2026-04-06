package com.iesjc.keymaster.controller;

import com.iesjc.keymaster.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * ENDPOINT 1: Generar Reporte Historial Semanal (PDF)
     * URL: GET http://localhost:8080/api/reportes/historial?inicio=2026-02-15&fin=2026-02-21
     */
    @GetMapping("/historial")
    public ResponseEntity<byte[]> generarHistorial(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        // Llamamos al servicio que devuelve los bytes del PDF
        byte[] pdfBytes = reporteService.generarReporteHistorialPdf(inicio, fin);

        // Configuramos las cabeceras HTTP para indicarle al cliente que es un PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Sugerir nombre de archivo por defecto al cliente
        headers.setContentDispositionFormData("attachment", "Historial_KeyMaster_" + inicio + "_al_" + fin + ".pdf");

        // Devolvemos el array de bytes con un código 200 OK
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}