package com.oficioYa.service.impl;

import com.oficioYa.exception.RecursoNoEncontradoException;
import com.oficioYa.model.domain.EstadoVerificacion;
import com.oficioYa.persistence.entity.PerfilTrabajadorEntity;
import com.oficioYa.repository.PerfilTrabajadorRepository;
import com.oficioYa.service.IStorageService;
import com.oficioYa.service.IVerificacionIdentidadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificacionIdentidadServiceImpl implements IVerificacionIdentidadService {

    private final PerfilTrabajadorRepository perfilTrabajadorRepository;
    private final IStorageService storageService;

    private PerfilTrabajadorEntity obtenerPerfil(Long trabajadorId) {
        return perfilTrabajadorRepository.findByUsuarioId(trabajadorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Perfil de trabajador no encontrado"));
    }

    private void verificarTransicionEstado(PerfilTrabajadorEntity perfil) {
        // Si todos los documentos estan subidos, pasa a PENDIENTE_REVISION
        if (perfil.getDocumentosIdentidadUrl() != null &&
            perfil.getCertificadoAntecedentesUrl() != null &&
            perfil.getFotoPerfilVerificadaUrl() != null) {
            
            if (perfil.getEstadoVerificacion() == EstadoVerificacion.NO_VERIFICADO ||
                perfil.getEstadoVerificacion() == EstadoVerificacion.RECHAZADO) {
                perfil.setEstadoVerificacion(EstadoVerificacion.PENDIENTE_REVISION);
                log.info("Perfil {} pasa a PENDIENTE_REVISION", perfil.getUsuario().getId());
            }
        }
    }

    @Override
    @Transactional
    public String subirDocumentosIdentidad(Long trabajadorId, MultipartFile documentoFrente, MultipartFile documentoReverso) {
        log.info("Subiendo documentos de identidad para trabajador {}", trabajadorId);
        PerfilTrabajadorEntity perfil = obtenerPerfil(trabajadorId);
        
        // En una implementacion real se combinarian o guardarian ambos
        String frenteUrl = storageService.guardarImagen(documentoFrente);
        String reversoUrl = storageService.guardarImagen(documentoReverso);
        
        String finalUrl = frenteUrl + ";" + reversoUrl; // Simple delimitador
        perfil.setDocumentosIdentidadUrl(finalUrl);
        verificarTransicionEstado(perfil);
        perfilTrabajadorRepository.save(perfil);
        return finalUrl;
    }

    @Override
    @Transactional
    public String subirCertificadoAntecedentes(Long trabajadorId, MultipartFile certificado) {
        log.info("Subiendo certificado de antecedentes para trabajador {}", trabajadorId);
        PerfilTrabajadorEntity perfil = obtenerPerfil(trabajadorId);
        
        String url = storageService.guardarImagen(certificado);
        perfil.setCertificadoAntecedentesUrl(url);
        verificarTransicionEstado(perfil);
        perfilTrabajadorRepository.save(perfil);
        return url;
    }

    @Override
    @Transactional
    public String subirFotoPerfilVerificada(Long trabajadorId, MultipartFile foto) {
        log.info("Subiendo foto de perfil verificada para trabajador {}", trabajadorId);
        PerfilTrabajadorEntity perfil = obtenerPerfil(trabajadorId);
        
        String url = storageService.guardarImagen(foto);
        perfil.setFotoPerfilVerificadaUrl(url);
        verificarTransicionEstado(perfil);
        perfilTrabajadorRepository.save(perfil);
        return url;
    }

    @Override
    public EstadoVerificacion obtenerEstadoVerificacion(Long trabajadorId) {
        return obtenerPerfil(trabajadorId).getEstadoVerificacion();
    }

    @Override
    @Transactional
    public void actualizarEstadoVerificacion(Long trabajadorId, EstadoVerificacion estado) {
        log.info("Actualizando estado de verificacion de {} a {}", trabajadorId, estado);
        PerfilTrabajadorEntity perfil = obtenerPerfil(trabajadorId);
        perfil.setEstadoVerificacion(estado);
        perfilTrabajadorRepository.save(perfil);
    }
}
