package com.iesjc.keymaster.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesorResponseDTO {

    private Integer idProfesor;
    private String dni;
    private String nombre;
    private String apellidos;
    private String email;

    // Dato aplanado
    private String nombreDepartamento;

    // Vital para la tabla de JavaFX para saber si colorear de rojo como "Dado de Baja"
    private Boolean activo;
}