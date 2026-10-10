package com.oficioYa.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PortafolioRequestDTO {
    
    @Schema(description = "Lista de URLs de imágenes alojadas en Cloudinary para el portafolio", example = "[\"https://res.cloudinary.com/demo/image/upload/sample.jpg\"]")
    @NotNull(message = "La lista de fotos no puede ser nula")
    @Size(max = 20, message = "No se permiten más de 20 fotos en el portafolio")
    private List<String> fotosPortafolio;
}
