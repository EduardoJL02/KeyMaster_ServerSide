package com.iesjc.keymaster.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private int status;         // ej. 400, 404, 409
    private String error;       // ej. "Bad Request", "Conflict"
    private String message;     // ej. "El profesor está dado de baja"
    private String path;        // ej. "/api/prestamos"
}