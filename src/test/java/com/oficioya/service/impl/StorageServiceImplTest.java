package com.oficioya.service.impl;

import com.oficioya.exception.EstadoInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceImplTest {

    @InjectMocks
    private StorageServiceImpl storageService;

    @Mock
    private MultipartFile file;

    @Test
    @DisplayName("guardarImagen - archivo vacío lanza EstadoInvalidoException")
    void guardarImagen_archivoVacio_lanzaExcepcion() {
        when(file.isEmpty()).thenReturn(true);

        EstadoInvalidoException ex = assertThrows(
                EstadoInvalidoException.class,
                () -> storageService.guardarImagen(file)
        );

        assertEquals("El archivo está vacío", ex.getMessage());
    }

    @Test
    @DisplayName("guardarImagen - tipo de contenido nulo o inválido lanza EstadoInvalidoException")
    void guardarImagen_tipoInvalido_lanzaExcepcion() {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");

        EstadoInvalidoException ex = assertThrows(
                EstadoInvalidoException.class,
                () -> storageService.guardarImagen(file)
        );

        assertEquals("El archivo debe ser una imagen (JPG, JPEG, PNG)", ex.getMessage());
    }

    @Test
    @DisplayName("guardarImagen - tipo de contenido nulo lanza EstadoInvalidoException")
    void guardarImagen_tipoNulo_lanzaExcepcion() {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn(null);

        EstadoInvalidoException ex = assertThrows(
                EstadoInvalidoException.class,
                () -> storageService.guardarImagen(file)
        );

        assertEquals("El archivo debe ser una imagen (JPG, JPEG, PNG)", ex.getMessage());
    }

    @Test
    @DisplayName("guardarImagen - archivo JPG válido guarda físicamente y retorna ruta")
    void guardarImagen_archivoValido_retornaRuta() throws IOException {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getOriginalFilename()).thenReturn("avatar.jpeg");
        doNothing().when(file).transferTo(any(File.class));

        String url = storageService.guardarImagen(file);

        assertNotNull(url);
        assertTrue(url.startsWith("/uploads/perfiles/"));
        assertTrue(url.endsWith(".jpeg"));
        verify(file).transferTo(any(File.class));
    }

    @Test
    @DisplayName("guardarImagen - archivo PNG sin extensión guarda correctamente")
    void guardarImagen_archivoSinExtension_retornaRuta() throws IOException {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getOriginalFilename()).thenReturn("avatar");
        doNothing().when(file).transferTo(any(File.class));

        String url = storageService.guardarImagen(file);

        assertNotNull(url);
        assertTrue(url.startsWith("/uploads/perfiles/"));
        verify(file).transferTo(any(File.class));
    }

    @Test
    @DisplayName("guardarImagen - error de I/O lanza RuntimeException")
    void guardarImagen_errorIO_lanzaRuntimeException() throws IOException {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getOriginalFilename()).thenReturn("foto.png");
        doThrow(new IOException("Disk error")).when(file).transferTo(any(File.class));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> storageService.guardarImagen(file)
        );

        assertTrue(ex.getMessage().contains("No se pudo guardar la imagen de perfil"));
    }
}
