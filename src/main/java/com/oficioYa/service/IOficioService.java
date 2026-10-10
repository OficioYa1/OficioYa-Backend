package com.oficioYa.service;

import com.oficioYa.model.dto.response.OficioResponseDTO;

import java.util.List;

public interface IOficioService {
    List<OficioResponseDTO> listarOficiosActivos();
}
