package com.iesjc.keymaster.service;

import com.iesjc.keymaster.dto.request.LlaveRequestDTO;
import com.iesjc.keymaster.dto.response.LlaveResponseDTO;

import java.util.List;

public interface LlaveService {
    List<LlaveResponseDTO> obtenerTodas();
    LlaveResponseDTO obtenerPorId(Integer idLlave);
    LlaveResponseDTO crearLlave(LlaveRequestDTO request);
    LlaveResponseDTO actualizarLlave(Integer idLlave, LlaveRequestDTO request);
    void eliminarLlave(Integer idLlave);
}