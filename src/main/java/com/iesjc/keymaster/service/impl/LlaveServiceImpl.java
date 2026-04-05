package com.iesjc.keymaster.service.impl;

import com.iesjc.keymaster.dto.request.LlaveRequestDTO;
import com.iesjc.keymaster.dto.response.LlaveResponseDTO;
import com.iesjc.keymaster.entity.Espacio;
import com.iesjc.keymaster.entity.EstadoLlave;
import com.iesjc.keymaster.entity.Llave;
import com.iesjc.keymaster.exception.ResourceNotFoundException;
import com.iesjc.keymaster.repository.EspacioRepository;
import com.iesjc.keymaster.repository.LlaveRepository;
import com.iesjc.keymaster.service.LlaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LlaveServiceImpl implements LlaveService {

    private final LlaveRepository llaveRepository;
    private final EspacioRepository espacioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LlaveResponseDTO> obtenerTodas() {
        return llaveRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LlaveResponseDTO obtenerPorId(Integer idLlave) {
        Llave llave = llaveRepository.findById(idLlave)
                .orElseThrow(() -> new ResourceNotFoundException("Llave no encontrada con ID: " + idLlave));
        return mapToDTO(llave);
    }

    @Override
    @Transactional
    public LlaveResponseDTO crearLlave(LlaveRequestDTO request) {
        // 1. Validar duplicidad de código interno (Vulnerabilidad 2 del PDF)
        Optional<Llave> llaveExistente = llaveRepository.findByCodigoInterno(request.getCodigoInterno());
        if (llaveExistente.isPresent()) {
            throw new DataIntegrityViolationException("El código de llave '" + request.getCodigoInterno() + "' ya existe en el sistema.");
        }

        // 2. Buscar el espacio asociado
        Espacio espacio = espacioRepository.findById(request.getIdEspacio())
                .orElseThrow(() -> new ResourceNotFoundException("El espacio seleccionado no existe"));

        // 3. Crear la entidad (Por defecto, al crear, siempre está DISPONIBLE)
        Llave nuevaLlave = Llave.builder()
                .codigoInterno(request.getCodigoInterno())
                .espacio(espacio)
                .estado(EstadoLlave.DISPONIBLE)
                .build();

        Llave llaveGuardada = llaveRepository.save(nuevaLlave);
        return mapToDTO(llaveGuardada);
    }

    @Override
    @Transactional
    public LlaveResponseDTO actualizarLlave(Integer idLlave, LlaveRequestDTO request) {
        // 1. Buscar la llave a editar
        Llave llave = llaveRepository.findById(idLlave)
                .orElseThrow(() -> new ResourceNotFoundException("Llave no encontrada con ID: " + idLlave));

        // 2. Validar duplicidad SOLO si está intentando cambiar el código por otro que ya existe
        if (!llave.getCodigoInterno().equals(request.getCodigoInterno())) {
            Optional<Llave> llaveExistente = llaveRepository.findByCodigoInterno(request.getCodigoInterno());
            if (llaveExistente.isPresent()) {
                throw new DataIntegrityViolationException("El código de llave '" + request.getCodigoInterno() + "' ya está en uso.");
            }
        }

        // 3. Buscar el espacio por si lo ha cambiado
        Espacio espacio = espacioRepository.findById(request.getIdEspacio())
                .orElseThrow(() -> new ResourceNotFoundException("El espacio seleccionado no existe"));

        // 4. Actualizar datos
        llave.setCodigoInterno(request.getCodigoInterno());
        llave.setEspacio(espacio);

        // Si el estado viene en la petición, lo actualizamos. Usamos valueOf para parsear el String.
        if (request.getEstado() != null && !request.getEstado().isEmpty()) {
            llave.setEstado(EstadoLlave.valueOf(request.getEstado().toUpperCase()));
        }

        Llave llaveActualizada = llaveRepository.save(llave);
        return mapToDTO(llaveActualizada);
    }

    @Override
    @Transactional
    public void eliminarLlave(Integer idLlave) {
        Llave llave = llaveRepository.findById(idLlave)
                .orElseThrow(() -> new ResourceNotFoundException("Llave no encontrada con ID: " + idLlave));

        // Validación crítica: Bloqueo por estado (Vulnerabilidad 1 del PDF)
        if (llave.getEstado() == EstadoLlave.EN_USO) {
            throw new IllegalStateException("No se puede eliminar la llave " + llave.getCodigoInterno() + " porque actualmente está EN USO (prestada).");
        }

        // Si está disponible, perdida o en mantenimiento, procedemos a borrarla
        llaveRepository.delete(llave);
    }

    // --- MÉTOD0 PRIVADO DE MAPEO (Aplanamiento de Entidades) ---
    private LlaveResponseDTO mapToDTO(Llave llave) {
        return LlaveResponseDTO.builder()
                .idLlave(llave.getIdLlave())
                .codigoInterno(llave.getCodigoInterno())
                .estado(llave.getEstado().name())
                .idEspacio(llave.getEspacio().getIdEspacio())
                .codigoEspacio(llave.getEspacio().getCodigo())
                .tipoEspacio(llave.getEspacio().getTipo().name())
                .descripcionEspacio(llave.getEspacio().getDescripcion())
                .build();
    }
}