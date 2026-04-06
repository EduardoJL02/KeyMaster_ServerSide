package com.iesjc.keymaster.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteMovimientoDTO {
    // Los nombres de estos campos DEBEN coincidir exactamente con los "Fields" que creemos en Jaspersoft Studio
    private String fechaHora;
    private String codigoLlave;
    private String espacio;
    private String nombreDocente;
    private String dniDocente;
    private String accion;
    private String conserje;
}