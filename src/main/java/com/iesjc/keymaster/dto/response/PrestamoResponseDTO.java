package com.iesjc.keymaster.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestamoResponseDTO {
    private Integer idPrestamo;

    // Datos aplanados de la Llave
    private String codigoLlave;
    private String espacio;

    // Datos aplanados del Profesor
    private String nombreProfesor;
    private String dniProfesor;

    // Datos del préstamo
    private LocalDateTime fechaSalida;
    private LocalTime fechaLimite;
    private LocalDateTime fechaEntrada;

    // Estado calculado al vuelo ("A TIEMPO", "VENCIDO", "DEVUELTO")
    private String estado;
}