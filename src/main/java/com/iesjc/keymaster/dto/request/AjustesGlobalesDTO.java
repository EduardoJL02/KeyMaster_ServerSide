package com.iesjc.keymaster.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AjustesGlobalesDTO {
    // Ajustes de Alertas
    private Boolean alertasHabilitadas;

    // Ajustes SMTP
    private String smtpHost;
    private String smtpPort;
    private String smtpUser;
    private String smtpPassword; // Enviaremos "******" desde el backend por seguridad
}