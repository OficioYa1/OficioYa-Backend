package com.oficioya.service;

import com.oficioya.model.dto.response.OficioResponseDTO;

import java.util.List;

public interface IOficioService {
    List<OficioResponseDTO> listarOficiosActivos();
}
