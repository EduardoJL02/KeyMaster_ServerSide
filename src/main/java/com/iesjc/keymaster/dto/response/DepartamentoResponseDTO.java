package com.iesjc.keymaster.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartamentoResponseDTO {
    private Integer idDepartamento;
    private String nombre;
}