package com.iesjc.keymaster.service.impl;
import com.iesjc.keymaster.dto.response.EspacioResponseDTO;
import com.iesjc.keymaster.repository.EspacioRepository;
import com.iesjc.keymaster.service.EspacioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EspacioServiceImpl implements EspacioService {
    private final EspacioRepository espacioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EspacioResponseDTO> obtenerTodos() {
        return espacioRepository.findAll().stream()
                .map(e -> new EspacioResponseDTO(e.getIdEspacio(), e.getCodigo(), e.getTipo().name(), e.getDescripcion()))
                .collect(Collectors.toList());
    }
}