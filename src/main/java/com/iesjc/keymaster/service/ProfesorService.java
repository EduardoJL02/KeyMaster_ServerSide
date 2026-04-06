package com.iesjc.keymaster.service;

import com.iesjc.keymaster.dto.request.ProfesorRequestDTO;
import com.iesjc.keymaster.dto.response.ProfesorResponseDTO;

import java.util.List;

public interface ProfesorService {
    // Permitimos filtrar si queremos ver solo los activos o también los dados de baja
    List<ProfesorResponseDTO> obtenerTodos(boolean incluirBajas);
    ProfesorResponseDTO obtenerPorId(Integer idProfesor);
    ProfesorResponseDTO crearProfesor(ProfesorRequestDTO request);
    ProfesorResponseDTO actualizarProfesor(Integer idProfesor, ProfesorRequestDTO request);
    void darDeBaja(Integer idProfesor); // Soft Delete
}