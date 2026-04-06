package com.iesjc.keymaster.service.impl;
import com.iesjc.keymaster.dto.response.DepartamentoResponseDTO;
import com.iesjc.keymaster.repository.DepartamentoRepository;
import com.iesjc.keymaster.service.DepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartamentoServiceImpl implements DepartamentoService {
    private final DepartamentoRepository departamentoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponseDTO> obtenerTodos() {
        return departamentoRepository.findAll().stream()
                .map(d -> new DepartamentoResponseDTO(d.getIdDepartamento(), d.getNombre()))
                .collect(Collectors.toList());
    }
}