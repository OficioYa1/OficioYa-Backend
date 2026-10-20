package com.oficioya.service.strategy;

import com.oficioya.persistence.entity.ResenaEntity;
import com.oficioya.service.strategy.impl.PromedioPonderadoRecienteStrategy;
import com.oficioya.service.strategy.impl.PromedioSimpleStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculoReputacionStrategyTest {

    private PromedioSimpleStrategy simpleStrategy;
    private PromedioPonderadoRecienteStrategy ponderadoStrategy;

    @BeforeEach
    void setUp() {
        // Arrange
        simpleStrategy = new PromedioSimpleStrategy();
        ponderadoStrategy = new PromedioPonderadoRecienteStrategy();
    }

    @Test
    @DisplayName("Promedio Simple debe calcular la media aritmética exacta")
    void promedioSimpleDebeCalcularMediaExacta() {
        // Arrange
        ResenaEntity r1 = new ResenaEntity();
        r1.setEstrellas(5);
        r1.setReportada(false);

        ResenaEntity r2 = new ResenaEntity();
        r2.setEstrellas(3);
        r2.setReportada(false);

        List<ResenaEntity> resenas = List.of(r1, r2);

        // Act
        Double resultado = simpleStrategy.calcularPromedio(resenas);

        // Assert
        assertEquals(4.0, resultado);
    }

    @Test
    @DisplayName("Promedio Simple debe excluir reseñas reportadas")
    void promedioSimpleDebeExcluirReportadas() {
        // Arrange
        ResenaEntity r1 = new ResenaEntity();
        r1.setEstrellas(5);
        r1.setReportada(false);

        ResenaEntity rReportada = new ResenaEntity();
        rReportada.setEstrellas(1);
        rReportada.setReportada(true);

        List<ResenaEntity> resenas = List.of(r1, rReportada);

        // Act
        Double resultado = simpleStrategy.calcularPromedio(resenas);

        // Assert
        assertEquals(5.0, resultado);
    }

    @Test
    @DisplayName("Promedio Simple debe retornar 0.0 para listas vacías o nulas")
    void promedioSimpleDebeRetornarCeroSiVacio() {
        // Act
        // Assert
        assertEquals(0.0, simpleStrategy.calcularPromedio(Collections.emptyList()));
        assertEquals(0.0, simpleStrategy.calcularPromedio(null));
    }

    @Test
    @DisplayName("Promedio Ponderado debe otorgar mayor peso a reseñas recientes")
    void promedioPonderadoDebePonderarRecientes() {
        // Arrange
        // Reseña reciente (5 estrellas) con peso 1.5
        ResenaEntity rReciente = new ResenaEntity();
        rReciente.setEstrellas(5);
        rReciente.setReportada(false);
        rReciente.setFechaCreacion(LocalDateTime.now().minusDays(5));

        // Reseña antigua (1 estrella) con peso 1.0
        ResenaEntity rAntigua = new ResenaEntity();
        rAntigua.setEstrellas(1);
        rAntigua.setReportada(false);
        rAntigua.setFechaCreacion(LocalDateTime.now().minusDays(90));

        // Cálculo esperado: (5*1.5 + 1*1.0) / (1.5 + 1.0) = (7.5 + 1.0) / 2.5 = 8.5 / 2.5 = 3.4
        List<ResenaEntity> resenas = List.of(rReciente, rAntigua);

        // Act
        Double resultado = ponderadoStrategy.calcularPromedio(resenas);

        // Assert
        assertEquals(3.4, resultado);
    }

    @Test
    @DisplayName("Promedio Ponderado debe retornar 0.0 si la lista está vacía")
    void promedioPonderadoDebeRetornarCeroSiVacio() {
        // Act
        // Assert
        assertEquals(0.0, ponderadoStrategy.calcularPromedio(Collections.emptyList()));
        assertEquals(0.0, ponderadoStrategy.calcularPromedio(null));
    }
}
