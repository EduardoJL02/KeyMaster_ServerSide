package com.iesjc.keymaster.service.impl;

import com.iesjc.keymaster.dto.request.AjustesGlobalesDTO;
import com.iesjc.keymaster.entity.ConfiguracionGlobal;
import com.iesjc.keymaster.repository.ConfiguracionGlobalRepository;
import com.iesjc.keymaster.service.ConfiguracionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
                // Vulnerabilidad 2 (Exposición SMTP): Nunca enviamos la contraseña real al frontend
                .smtpPassword(getValorSeguro("SMTP_PASS", "").isEmpty() ? "" : "********")
                .build();
    }

    @Override
    @Transactional
    public AjustesGlobalesDTO guardarAjustes(AjustesGlobalesDTO request) {
        guardarOActualizar("ALERTAS_HABILITADAS", request.getAlertasHabilitadas().toString());
        guardarOActualizar("SMTP_HOST", request.getSmtpHost());
        guardarOActualizar("SMTP_PORT", request.getSmtpPort());
        guardarOActualizar("SMTP_USER", request.getSmtpUser());

        // Si el usuario no ha tocado la contraseña (sigue enviando "********"), no la machacamos
        if (request.getSmtpPassword() != null && !request.getSmtpPassword().equals("********")) {
            // Aquí en el futuro aplicaríamos un cifrado simétrico como AES-256
            guardarOActualizar("SMTP_PASS", request.getSmtpPassword());
        }

        return obtenerAjustes();
    }

    @Override
    public byte[] generarBackupBaseDatos() {
        // En un entorno de producción real, aquí ejecutaríamos Runtime.getRuntime().exec("mysqldump...")
        // Para este proyecto, generamos un archivo SQL simulado/exportación de prueba
        // para asegurar que el flujo de descarga en JavaFX funciona en cualquier ordenador sin depender del Path de MySQL.

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        StringBuilder fakeSqlDump = new StringBuilder();
        fakeSqlDump.append("-- Backup KeyMaster Center Generado Automáticamente\n");
        fakeSqlDump.append("-- Fecha: ").append(fecha).append("\n\n");
        fakeSqlDump.append("SET FOREIGN_KEY_CHECKS=0;\n");
        fakeSqlDump.append("-- (El volcado real de tablas se implementará con la herramienta mysqldump del SO)\n");
        fakeSqlDump.append("SET FOREIGN_KEY_CHECKS=1;\n");

        return fakeSqlDump.toString().getBytes();
    }

    // --- Métodos de Apoyo para Clave-Valor ---
    private String getValorSeguro(String clave, String valorPorDefecto) {
        return configuracionRepository.findByClave(clave)
                .map(ConfiguracionGlobal::getValor)
                .orElse(valorPorDefecto);
    }

    private void guardarOActualizar(String clave, String valor) {
        if (valor == null) return;
        ConfiguracionGlobal conf = configuracionRepository.findByClave(clave)
                .orElse(ConfiguracionGlobal.builder().clave(clave).build());
        conf.setValor(valor);
        configuracionRepository.save(conf);
    }
}