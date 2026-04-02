package com.iesjc.keymaster.service;

import com.iesjc.keymaster.dto.request.PrestamoCreateDTO;
// Importaremos un DTO de respuesta luego, por ahora devolvemos un String o Object
// import com.iesjc.keymaster.dto.response.PrestamoResponseDTO;

public interface PrestamoService {
    // El usernameConserje lo pasaremos desde el Controller, tras leer el JWT
    Object registrarNuevoPrestamo(PrestamoCreateDTO request, String usernameConserje);
}