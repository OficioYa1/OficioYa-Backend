package com.oficioya.service.impl;

import com.oficioya.model.domain.Solicitud;
import com.oficioya.model.exception.EstadoInvalidoException;
import com.oficioya.persistence.entity.EstadoSolicitud;
import com.oficioya.repository.SolicitudRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceImplTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private SolicitudServiceImpl service;

    @Test
    void enviarSolicitud_desdeCreada_debeSerExitoso() {
        // En una refactorización este test será inyectando el Mapper y configurando el mock de repositorio.
        service.enviarSolicitud(1L, 2L);
    }

    @Test
    void iniciarSolicitud_desdeCreada_debeLanzarExcepcion() {
        // Esta prueba requiere que Solicitud sea configurada correctamente en el Service, lo cual en getSolicitudDummy por defecto está en CREADA.
        assertThrows(EstadoInvalidoException.class, () -> {
            service.iniciarSolicitud(1L);
        });
    }
}
