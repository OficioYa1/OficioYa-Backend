package com.oficioya.service.impl;

import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        // ADAPTER PATTERN (MOCK): Simulación de subida a Cloudinary
        // No guardamos físicamente en disco, sino que devolvemos una URL simulada de la nube.
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") ? 
                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        
        String uniqueFilename = UUID.randomUUID().toString() + extension;
        
        log.info("Mock Cloudinary: Simulando subida del archivo {}...", originalFilename);
        
        // Simular un pequeño retardo de red (opcional)
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String fakeCloudinaryUrl = "https://mock.cloudinary.com/oficioya/image/upload/v1/" + uniqueFilename;
        log.info("Mock Cloudinary: Archivo subido exitosamente a la nube. URL generada: {}", fakeCloudinaryUrl);

        return fakeCloudinaryUrl;
    }
}
