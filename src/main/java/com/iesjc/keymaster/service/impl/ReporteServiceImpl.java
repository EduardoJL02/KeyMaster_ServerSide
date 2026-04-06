package com.iesjc.keymaster.service.impl;

import com.iesjc.keymaster.dto.response.ReporteMovimientoDTO;
import com.iesjc.keymaster.entity.Prestamo;
import com.iesjc.keymaster.repository.PrestamoRepository;
import com.iesjc.keymaster.service.ReporteService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final PrestamoRepository prestamoRepository;

    @Override
    public byte[] generarReporteHistorialPdf(LocalDate fechaInicio, LocalDate fechaFin) {

        // 1. Validación de seguridad (Prevenir OutOfMemory definido en el PDF)
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        if (ChronoUnit.DAYS.between(fechaInicio, fechaFin) > 90) {
            throw new IllegalArgumentException("El rango máximo permitido es de 90 días por informe para evitar saturación.");
        }

        // 2. Ajustar horas (Desde las 00:00:00 del inicio hasta las 23:59:59 del fin)
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        // 3. Obtener datos de MySQL
        List<Prestamo> prestamos = prestamoRepository.findByFechaSalidaBetweenOrderByFechaSalidaDesc(inicio, fin);

        // 4. Mapear Entidades a DTOs Planos para JasperReports
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<ReporteMovimientoDTO> datasource = prestamos.stream().map(p -> ReporteMovimientoDTO.builder()
                .fechaHora(p.getFechaSalida().format(formatter))
                .codigoLlave(p.getLlave().getCodigoInterno())
                .espacio(p.getLlave().getEspacio().getCodigo())
                .nombreDocente(p.getProfesor().getNombre() + " " + p.getProfesor().getApellidos())
                .dniDocente(p.getProfesor().getDni())
                .accion(p.getFechaEntrada() == null ? "Préstamo (Activo)" : "Préstamo y Devolución")
                .conserje(p.getUsuarioSalida().getUsername())
                .build()
        ).collect(Collectors.toList());

        try {
            // 5. Cargar la plantilla compilada (.jasper) desde la carpeta resources
            InputStream reporteStream = new ClassPathResource("reportes/historial_movimientos.jasper").getInputStream();

            // 6. Configurar Parámetros Adicionales (Títulos, Fechas del reporte, etc.)
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("RANGO_FECHAS", "Periodo: " + fechaInicio.toString() + " a " + fechaFin.toString());

            // 7. Rellenar el reporte con los datos
            JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(datasource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporteStream, parametros, jrDataSource);

            // 8. Exportar a array de bytes (PDF)
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte PDF: " + e.getMessage(), e);
        }
    }
}