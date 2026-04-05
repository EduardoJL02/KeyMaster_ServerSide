package com.iesjc.keymaster.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LlaveResponseDTO {

    private Integer idLlave;
    private String codigoInterno;
    private String estado; // "DISPONIBLE", "EN_USO", "PERDIDA", "MANTENIMIENTO"

    // Datos aplanados del Espacio (Evitamos enviar la entidad Espacio completa)
    private Integer idEspacio;
    private String codigoEspacio;       // ej. "A-301"
    private String tipoEspacio;         // ej. "Aula"
    private String descripcionEspacio;  // ej. "Aula de Informática Avanzada"
}