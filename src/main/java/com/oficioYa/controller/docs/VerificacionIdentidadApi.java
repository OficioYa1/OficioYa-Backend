package com.oficioYa.controller.docs;

import com.oficioYa.model.domain.EstadoVerificacion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Verificacion de Identidad", description = "Operaciones de verificacion de identidad para trabajadores")
public interface VerificacionIdentidadApi {

    @Operation(summary = "Subir documentos de identidad (frente y reverso)")
    @PostMapping(value = "/{trabajadorId}/documentos", consumes = "multipart/form-data")
    ResponseEntity<String> subirDocumentosIdentidad(
            @PathVariable Long trabajadorId,
            @RequestPart("frente") MultipartFile documentoFrente,
            @RequestPart("reverso") MultipartFile documentoReverso);

    @Operation(summary = "Subir certificado de antecedentes")
    @PostMapping(value = "/{trabajadorId}/antecedentes", consumes = "multipart/form-data")
    ResponseEntity<String> subirCertificadoAntecedentes(
            @PathVariable Long trabajadorId,
            @RequestPart("certificado") MultipartFile certificado);

    @Operation(summary = "Subir foto de perfil verificada")
    @PostMapping(value = "/{trabajadorId}/foto-verificada", consumes = "multipart/form-data")
    ResponseEntity<String> subirFotoPerfilVerificada(
            @PathVariable Long trabajadorId,
            @RequestPart("foto") MultipartFile foto);

    @Operation(summary = "Obtener el estado actual de verificacion")
    @GetMapping("/{trabajadorId}/estado")
    ResponseEntity<EstadoVerificacion> obtenerEstadoVerificacion(@PathVariable Long trabajadorId);
}
