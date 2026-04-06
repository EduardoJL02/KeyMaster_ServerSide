package com.iesjc.keymaster.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspacioResponseDTO {
    private Integer idEspacio;
    private String codigo; // ej. "A-301"
    private String tipo;   // ej. "AULA"
    private String descripcion;
}