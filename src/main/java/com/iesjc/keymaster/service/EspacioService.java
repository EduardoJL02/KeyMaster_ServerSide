package com.iesjc.keymaster.service;
import com.iesjc.keymaster.dto.response.EspacioResponseDTO;
import java.util.List;

public interface EspacioService {
    List<EspacioResponseDTO> obtenerTodos();
}