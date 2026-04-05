package com.iesjc.keymaster.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LlaveRequestDTO {

    @NotBlank(message = "El código interno de la llave es obligatorio (ej. LL-001)")
    private String codigoInterno;

    @NotNull(message = "Debe asignar un espacio válido a la llave")
    private Integer idEspacio;

    // Lo dejamos como String para que Jackson lo parsee fácilmente desde el JSON.
    // En la creación suele ser DISPONIBLE por defecto, pero nos servirá para la edición.
    private String estado;
}