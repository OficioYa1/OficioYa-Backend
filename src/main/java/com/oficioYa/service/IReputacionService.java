package com.oficioya.service;

import com.oficioya.model.dto.request.ReporteResenaRequestDTO;
import com.oficioya.model.dto.request.ResenaRequestDTO;
import com.oficioya.model.dto.response.ReputacionPorOficioDTO;
import com.oficioya.model.dto.response.ResenaResponseDTO;

import java.util.List;

public interface IReputacionService {

    ResenaResponseDTO calificarTrabajador(Long solicitudId, Long contratanteId, ResenaRequestDTO request);

    ResenaResponseDTO calificarContratante(Long solicitudId, Long trabajadorId, ResenaRequestDTO request);

    void reportarResena(Long resenaId, ReporteResenaRequestDTO request);

    ReputacionPorOficioDTO obtenerReputacionPorOficio(Long trabajadorId, Long oficioId);

    List<ResenaResponseDTO> obtenerResenasDeUsuario(Long usuarioId);
}
