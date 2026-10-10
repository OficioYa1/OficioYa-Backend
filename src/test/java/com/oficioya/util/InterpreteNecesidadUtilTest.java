package com.oficioYa.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InterpreteNecesidadUtilTest {

    @Test
    @DisplayName("extraerTerminos - 'alguien que arregle una gotera' se traduce a plomería")
    void extraerTerminos_gotera_devuelveRaizDePlomeria() {
        // Arrange
        String texto = "alguien que arregle una gotera";

        // Act
        List<String> terminos = InterpreteNecesidadUtil.extraerTerminos(texto);

        // Assert
        assertEquals(List.of("plom"), terminos);
    }

    @Test
    @DisplayName("extraerTerminos - ignora acentos, mayúsculas y signos")
    void extraerTerminos_acentosYSignos_losNormaliza() {
        // Act
        List<String> terminos = InterpreteNecesidadUtil.extraerTerminos("¡Necesito un ELECTRICISTA, urgente!");

        // Assert
        assertEquals(List.of("elec"), terminos);
    }

    @Test
    @DisplayName("extraerTerminos - no repite términos")
    void extraerTerminos_sinonimosRepetidos_devuelveUnoSolo() {
        // Act
        List<String> terminos = InterpreteNecesidadUtil.extraerTerminos("fuga en el grifo y el inodoro");

        // Assert
        assertEquals(List.of("plom"), terminos);
    }

    @Test
    @DisplayName("extraerTerminos - palabra sin sinónimo usa su raíz de 4 letras")
    void extraerTerminos_palabraLibre_usaRaiz() {
        // Act
        List<String> terminos = InterpreteNecesidadUtil.extraerTerminos("soldador");

        // Assert
        assertEquals(List.of("sold"), terminos);
    }

    @Test
    @DisplayName("extraerTerminos - texto nulo, vacío o solo palabras vacías devuelve lista vacía")
    void extraerTerminos_sinContenido_devuelveListaVacia() {
        // Act & Assert
        assertTrue(InterpreteNecesidadUtil.extraerTerminos(null).isEmpty());
        assertTrue(InterpreteNecesidadUtil.extraerTerminos("   ").isEmpty());
        assertTrue(InterpreteNecesidadUtil.extraerTerminos("necesito alguien para una").isEmpty());
    }

    @Test
    @DisplayName("normalizar - quita acentos y signos")
    void normalizar_textoConAcentos_devuelveTextoLimpio() {
        // Act
        String resultado = InterpreteNecesidadUtil.normalizar("Plomería, Hogar!");

        // Assert
        assertEquals("plomeria hogar", resultado);
    }
}
