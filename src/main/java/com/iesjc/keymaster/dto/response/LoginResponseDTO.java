package com.iesjc.keymaster.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String token; // Aquí irá el JWT que generaremos más adelante
    private String username;
    private String rol; // Enviaremos "CONSERJE" o "JEFATURA"

    // Opcional pero muy profesional: enviar un mensaje de éxito
    private String message;
}