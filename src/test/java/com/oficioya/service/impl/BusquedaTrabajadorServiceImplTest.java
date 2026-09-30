package com.oficioya.service.impl;

import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.repository.PerfilTrabajadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusquedaTrabajadorServiceImplTest {

    @Mock
    private PerfilTrabajadorRepository repository;

    @InjectMocks
    private BusquedaTrabajadorServiceImpl service;

    @Test
    void buscarTrabajadores_debeFiltrarPorZonaYCalificacion() {
        // ... (Test method)
    }
}
