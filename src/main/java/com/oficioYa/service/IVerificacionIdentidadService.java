package com.oficioya.service;

import com.oficioya.model.domain.EstadoVerificacion;
import org.springframework.web.multipart.MultipartFile;

public interface IVerificacionIdentidadService {
    
    // RF-40
    String subirDocumentosIdentidad(Long trabajadorId, MultipartFile documentoFrente, MultipartFile documentoReverso);
    
    // RF-41
    String subirCertificadoAntecedentes(Long trabajadorId, MultipartFile certificado);
    
    // RF-42
    String subirFotoPerfilVerificada(Long trabajadorId, MultipartFile foto);
    
    // RF-43
    EstadoVerificacion obtenerEstadoVerificacion(Long trabajadorId);
    
    // Solo interno o Admin/Moderacion (Si se necesita)
    void actualizarEstadoVerificacion(Long trabajadorId, EstadoVerificacion estado);
}
