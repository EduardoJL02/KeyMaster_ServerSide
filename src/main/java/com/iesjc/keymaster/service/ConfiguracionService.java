package com.iesjc.keymaster.service;

import com.iesjc.keymaster.dto.request.AjustesGlobalesDTO;

public interface ConfiguracionService {
    AjustesGlobalesDTO obtenerAjustes();
    AjustesGlobalesDTO guardarAjustes(AjustesGlobalesDTO request);
    byte[] generarBackupBaseDatos();
}