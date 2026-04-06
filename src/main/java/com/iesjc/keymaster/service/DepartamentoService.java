package com.iesjc.keymaster.service;
import com.iesjc.keymaster.dto.response.DepartamentoResponseDTO;
import java.util.List;

public interface DepartamentoService {
    List<DepartamentoResponseDTO> obtenerTodos();
}
