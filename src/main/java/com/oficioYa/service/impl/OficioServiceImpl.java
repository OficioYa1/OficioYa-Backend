package com.oficioYa.service.impl;

import com.oficioYa.mapper.OficioMapper;
import com.oficioYa.model.dto.response.OficioResponseDTO;
import com.oficioYa.persistence.entity.OficioEntity;
import com.oficioYa.repository.OficioRepository;
import com.oficioYa.service.IOficioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OficioServiceImpl implements IOficioService {

    private final OficioRepository oficioRepository;
    private final OficioMapper oficioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OficioResponseDTO> listarOficiosActivos() {
        log.info("Consultando la lista de oficios activos");
        
        List<OficioEntity> oficios = oficioRepository.findAll().stream()
                .filter(OficioEntity::isActivo)
                .collect(Collectors.toList());
                
        log.info("Se encontraron {} oficios activos", oficios.size());
        return oficioMapper.toDtoList(oficios);
    }
}
