package com.iesjc.keymaster.service;

import java.time.LocalDate;

public interface ReporteService {
    byte[] generarReporteHistorialPdf(LocalDate fechaInicio, LocalDate fechaFin);
}