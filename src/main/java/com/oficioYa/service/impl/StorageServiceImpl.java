package com.oficioya.service.impl;

import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class StorageServiceImpl implements IStorageService {

    private final String uploadDir = "uploads/perfiles/";

    @Override
    public String guardarImagen(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EstadoInvalidoException("El archivo está vacío");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/jpg"))) {
            throw new EstadoInvalidoException("El archivo debe ser una imagen (JPG, JPEG, PNG)");
        }

        try {
            // Crear el directorio si no existe
            // Se usa user.dir para asegurar que se guarde en la raíz del proyecto y no en la carpeta temporal de Tomcat
            Path rootPath = Paths.get(System.getProperty("user.dir"));
            Path uploadPath = rootPath.resolve(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generar un nombre único para evitar colisiones
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // Guardar el archivo físicamente
            Path filePath = uploadPath.resolve(uniqueFilename);
            file.transferTo(filePath.toFile());

            log.info("Archivo guardado exitosamente: {}", filePath.toString());

            // Devolver la URL virtual para que el frontend pueda consumirla luego
            return "/" + uploadDir + uniqueFilename;

        } catch (IOException e) {
            log.error("Error al guardar el archivo", e);
            throw new RuntimeException("No se pudo guardar la imagen de perfil", e);
        }
    }
}
