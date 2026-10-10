package com.oficioYa.service;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    /**
     * Guarda un archivo de imagen en el almacenamiento local.
     * @param file El archivo a guardar.
     * @return La URL o ruta relativa donde se guardó el archivo.
     */
    String guardarImagen(MultipartFile file);
}
