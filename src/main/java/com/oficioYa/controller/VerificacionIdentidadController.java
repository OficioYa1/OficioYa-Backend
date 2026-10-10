package com.oficioYa.controller;

import com.oficioYa.controller.docs.VerificacionIdentidadApi;
import com.oficioYa.model.domain.EstadoVerificacion;
import com.oficioYa.service.IVerificacionIdentidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/trabajadores/verificacion")
@RequiredArgsConstructor
public class VerificacionIdentidadController implements VerificacionIdentidadApi {

    private final IVerificacionIdentidadService verificacionIdentidadService;

    @Override
    public ResponseEntity<String> subirDocumentosIdentidad(Long trabajadorId, MultipartFile documentoFrente, MultipartFile documentoReverso) {
        String url = verificacionIdentidadService.subirDocumentosIdentidad(trabajadorId, documentoFrente, documentoReverso);
        return ResponseEntity.ok(url);
    }

    @Override
    public ResponseEntity<String> subirCertificadoAntecedentes(Long trabajadorId, MultipartFile certificado) {
        String url = verificacionIdentidadService.subirCertificadoAntecedentes(trabajadorId, certificado);
        return ResponseEntity.ok(url);
    }

    @Override
    public ResponseEntity<String> subirFotoPerfilVerificada(Long trabajadorId, MultipartFile foto) {
        String url = verificacionIdentidadService.subirFotoPerfilVerificada(trabajadorId, foto);
        return ResponseEntity.ok(url);
    }

    @Override
    public ResponseEntity<EstadoVerificacion> obtenerEstadoVerificacion(Long trabajadorId) {
        return ResponseEntity.ok(verificacionIdentidadService.obtenerEstadoVerificacion(trabajadorId));
    }
}
