package com.oficioya.validator;

import com.oficioya.model.domain.CriteriosBusqueda;
import com.oficioya.model.exception.ReglaDeNegocioException;
import com.oficioya.validator.impl.BusquedaValidatorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BusquedaValidatorImplTest {

    private final BusquedaValidatorImpl validator = new BusquedaValidatorImpl();

    @Test
    @DisplayName("validarRangoTarifa - min menor o igual a max no lanza excepción")
    void validarRangoTarifa_rangoValido_noLanza() {
        // Arrange
        CriteriosBusqueda c = CriteriosBusqueda.builder().tarifaMin(new BigDecimal("10000")).tarifaMax(new BigDecimal("10000")).build();

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarRangoTarifa(c));
    }

    @Test
    @DisplayName("validarRangoTarifa - solo un límite no lanza excepción")
    void validarRangoTarifa_unSoloLimite_noLanza() {
        // Arrange
        CriteriosBusqueda c = CriteriosBusqueda.builder().tarifaMax(new BigDecimal("50000")).build();

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarRangoTarifa(c));
    }

    @Test
    @DisplayName("validarRangoTarifa - min mayor que max lanza ReglaDeNegocioException")
    void validarRangoTarifa_minMayorQueMax_lanzaReglaDeNegocio() {
        // Arrange
        CriteriosBusqueda c = CriteriosBusqueda.builder().tarifaMin(new BigDecimal("60000")).tarifaMax(new BigDecimal("20000")).build();

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> validator.validarRangoTarifa(c));
    }

    @Test
    @DisplayName("validarFranjaSolicitada - sin horas ni día no lanza excepción")
    void validarFranja_sinFiltroHorario_noLanza() {
        // Arrange
        CriteriosBusqueda c = CriteriosBusqueda.builder().build();

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarFranjaSolicitada(c));
    }

    @Test
    @DisplayName("validarFranjaSolicitada - día con rango válido no lanza excepción")
    void validarFranja_diaYRangoValido_noLanza() {
        // Arrange
        CriteriosBusqueda c = CriteriosBusqueda.builder().dia(DayOfWeek.MONDAY)
                .horaDesde(LocalTime.of(9, 0)).horaHasta(LocalTime.of(11, 0)).build();

        // Act & Assert
        assertDoesNotThrow(() -> validator.validarFranjaSolicitada(c));
    }

    @Test
    @DisplayName("validarFranjaSolicitada - horas sin día lanza ReglaDeNegocioException")
    void validarFranja_horasSinDia_lanzaReglaDeNegocio() {
        // Arrange
        CriteriosBusqueda c = CriteriosBusqueda.builder().horaDesde(LocalTime.of(9, 0)).build();

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> validator.validarFranjaSolicitada(c));
    }

    @Test
    @DisplayName("validarFranjaSolicitada - horaHasta sin horaDesde lanza ReglaDeNegocioException")
    void validarFranja_soloHoraHasta_lanzaReglaDeNegocio() {
        // Arrange
        CriteriosBusqueda c = CriteriosBusqueda.builder().dia(DayOfWeek.MONDAY).horaHasta(LocalTime.of(11, 0)).build();

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> validator.validarFranjaSolicitada(c));
    }

    @Test
    @DisplayName("validarFranjaSolicitada - desde igual o posterior a hasta lanza ReglaDeNegocioException")
    void validarFranja_rangoInvertido_lanzaReglaDeNegocio() {
        // Arrange
        CriteriosBusqueda c = CriteriosBusqueda.builder().dia(DayOfWeek.MONDAY)
                .horaDesde(LocalTime.of(11, 0)).horaHasta(LocalTime.of(9, 0)).build();

        // Act & Assert
        assertThrows(ReglaDeNegocioException.class, () -> validator.validarFranjaSolicitada(c));
    }
}
