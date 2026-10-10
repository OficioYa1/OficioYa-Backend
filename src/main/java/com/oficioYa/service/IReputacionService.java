package com.oficioYa.service;

import com.oficioYa.model.dto.request.ReporteResenaRequestDTO;
import com.oficioYa.model.dto.request.ResenaRequestDTO;
import com.oficioYa.model.dto.response.ReputacionPorOficioDTO;
import com.oficioYa.model.dto.response.ResenaResponseDTO;

import java.util.List;

public interface IReputacionService {

    ResenaResponseDTO calificarTrabajador(Long solicitudId, Long contratanteId, ResenaRequestDTO request);

    ResenaResponseDTO calificarContratante(Long solicitudId, Long trabajadorId, ResenaRequestDTO request);

    void reportarResena(Long resenaId, ReporteResenaRequestDTO request);

    ReputacionPorOficioDTO obtenerReputacionPorOficio(Long trabajadorId, Long oficioId);

    List<ResenaResponseDTO> obtenerResenasDeUsuario(Long usuarioId);
}
