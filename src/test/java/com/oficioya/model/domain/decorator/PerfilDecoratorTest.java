package com.oficioya.model.domain.decorator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerfilDecoratorTest {

    @Test
    @DisplayName("Perfil base sin decoradores debe tener valores estándar")
    void perfilBaseDebeTenerValoresEstandar() {
        // Arrange
        String nombre = "Carlos Plomero";

        // Act
        IPerfilVisualComponent perfil = PerfilVisualBuilder.paraTrabajador(nombre).build();

        // Assert
        assertEquals("Carlos Plomero", perfil.getNombreVisible());
        assertEquals("Estándar", perfil.getBadgeDestacado());
        assertFalse(perfil.esAltamenteConfiable());
        assertTrue(perfil.getInsignias().contains("Perfil Básico"));
        assertEquals(1, perfil.getInsignias().size());
    }

    @Test
    @DisplayName("Debe decorar perfil con Identidad Verificada")
    void debeDecorarConIdentidadVerificada() {
        // Arrange
        String nombre = "Ana Electricista";

        // Act
        IPerfilVisualComponent perfil = PerfilVisualBuilder.paraTrabajador(nombre)
                .conIdentidadVerificada(true)
                .build();

        // Assert
        assertTrue(perfil.esAltamenteConfiable());
        assertEquals("Oficialmente Verificado", perfil.getBadgeDestacado());
        assertTrue(perfil.getInsignias().contains("Identidad Verificada (Cédula Auditada)"));
        assertEquals(2, perfil.getInsignias().size());
    }

    @Test
    @DisplayName("Debe acumular múltiples decoradores (Identidad + Referente + Disponible Ahora)")
    void debeAcumularMultiplesDecoradores() {
        // Arrange
        String nombre = "Pedro Maestro";

        // Act
        IPerfilVisualComponent perfil = PerfilVisualBuilder.paraTrabajador(nombre)
                .conIdentidadVerificada(true)
                .conReferenteComunitario(true)
                .conDisponibleAhora(true)
                .build();

        // Assert
        List<String> insignias = perfil.getInsignias();
        assertEquals(4, insignias.size());
        assertTrue(insignias.contains("Perfil Básico"));
        assertTrue(insignias.contains("Identidad Verificada (Cédula Auditada)"));
        assertTrue(insignias.contains("Referente Comunitario Verificado (Conector Vecinal)"));
        assertTrue(insignias.contains("Disponible Ahora (Atención Inmediata)"));
        assertTrue(perfil.esAltamenteConfiable());
        assertEquals("⚡ Disponible Inmediato", perfil.getBadgeDestacado());
    }

    @Test
    @DisplayName("Decorador base debe rechazar componente nulo")
    void decoradorDebeRechazarComponenteNulo() {
        // Arrange
        IPerfilVisualComponent componenteNulo = null;

        // Act
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new IdentidadVerificadaDecorator(componenteNulo));
    }
}
