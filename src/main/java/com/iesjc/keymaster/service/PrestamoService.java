package com.iesjc.keymaster.service;

import com.iesjc.keymaster.dto.request.PrestamoCreateDTO;
import com.iesjc.keymaster.dto.response.PrestamoResponseDTO;
import java.util.List;

public interface PrestamoService {
    PrestamoResponseDTO registrarNuevoPrestamo(PrestamoCreateDTO request, String usernameConserje);
    List<PrestamoResponseDTO> obtenerActivos();
    PrestamoResponseDTO registrarDevolucion(Integer idPrestamo, String usernameConserje);
}