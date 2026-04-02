package com.iesjc.keymaster.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestamoCreateDTO {

    @NotNull(message = "El ID de la llave es obligatorio")
    private Integer idLlave;

    @NotNull(message = "El ID del profesor es obligatorio")
    private Integer idProfesor;

    // Puede ser null si el conserje seleccionó "Sin límite" en el ComboBox
    private LocalTime fechaLimite;
}