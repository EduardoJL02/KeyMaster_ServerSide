package com.iesjc.keymaster.service.impl;

import com.iesjc.keymaster.dto.request.AjustesGlobalesDTO;
import com.iesjc.keymaster.entity.ConfiguracionGlobal;
import com.iesjc.keymaster.repository.ConfiguracionGlobalRepository;
import com.iesjc.keymaster.service.ConfiguracionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final ConfiguracionGlobalRepository configuracionRepository;

    @Override
    @Transactional(readOnly = true)
    public AjustesGlobalesDTO obtenerAjustes() {
        return AjustesGlobalesDTO.builder()
                .alertasHabilitadas(Boolean.parseBoolean(getValorSeguro("ALERTAS_HABILITADAS", "false")))
                .smtpHost(getValorSeguro("SMTP_HOST", ""))
                .smtpPort(getValorSeguro("SMTP_PORT", "587"))
                .smtpUser(getValorSeguro("SMTP_USER", ""))
                // Por seguridad, si hay contraseña guardada, enviamos un enmascaramiento al frontend
                .smtpPassword(getValorSeguro("SMTP_PASS", "").isEmpty() ? "" : "********")
                .build();
    }

    @Override
    @Transactional
    public AjustesGlobalesDTO guardarAjustes(AjustesGlobalesDTO request) {
        guardarOActualizar("ALERTAS_HABILITADAS", String.valueOf(request.getAlertasHabilitadas()));
        guardarOActualizar("SMTP_HOST", request.getSmtpHost());
        guardarOActualizar("SMTP_PORT", request.getSmtpPort());
        guardarOActualizar("SMTP_USER", request.getSmtpUser());

        // Evitamos sobreescribir la contraseña real con los asteriscos del frontend
        if (request.getSmtpPassword() != null && !request.getSmtpPassword().equals("********")) {
            // Aquí en el futuro aplicaríamos un cifrado simétrico como AES-256
            guardarOActualizar("SMTP_PASS", request.getSmtpPassword());
        }

        // Devolvemos el estado final guardado
        return obtenerAjustes();
    }

    @Override
    public byte[] generarBackupBaseDatos() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Simulamos el contenido de un volcado SQL para asegurar la compatibilidad multiplataforma
        StringBuilder sqlDump = new StringBuilder();
        sqlDump.append("-- ==================================================\n");
        sqlDump.append("-- BACKUP KEYMASTER CENTER\n");
        sqlDump.append("-- Generado el: ").append(fecha).append("\n");
        sqlDump.append("-- Rol que ejecuta: JEFATURA\n");
        sqlDump.append("-- ==================================================\n\n");

        sqlDump.append("SET FOREIGN_KEY_CHECKS=0;\n\n");

        sqlDump.append("-- Volcado de estructura para la tabla 'llave'\n");
        sqlDump.append("DROP TABLE IF EXISTS `llave`;\n");
        sqlDump.append("CREATE TABLE `llave` (\n");
        sqlDump.append("  `id_llave` int NOT NULL AUTO_INCREMENT,\n");
        sqlDump.append("  `codigo_interno` varchar(50) NOT NULL,\n");
        sqlDump.append("  `estado` varchar(20) DEFAULT 'DISPONIBLE',\n");
        sqlDump.append("  PRIMARY KEY (`id_llave`)\n");
        sqlDump.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n\n");

        sqlDump.append("-- (Nota: Este es un volcado simulado para la demostración del proyecto)\n\n");
        sqlDump.append("SET FOREIGN_KEY_CHECKS=1;\n");

        return sqlDump.toString().getBytes();
    }

    // --- MÉTODOS PRIVADOS AUXILIARES ---

    private String getValorSeguro(String clave, String valorPorDefecto) {
        return configuracionRepository.findByClave(clave)
                .map(ConfiguracionGlobal::getValor)
                .orElse(valorPorDefecto);
    }

    private void guardarOActualizar(String clave, String valor) {
        if (valor == null) return;

        ConfiguracionGlobal config = configuracionRepository.findByClave(clave)
                .orElse(ConfiguracionGlobal.builder().clave(clave).build());

        config.setValor(valor);
        configuracionRepository.save(config);
    }
}