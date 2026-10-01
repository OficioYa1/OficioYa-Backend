package com.oficioya.mapper;

import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.dto.request.PerfilTrabajadorCreacionRequestDTO;
import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class PerfilTrabajadorMapperTest {

    private final PerfilTrabajadorMapper mapper = Mappers.getMapper(PerfilTrabajadorMapper.class);

    @Test
    @DisplayName("toDomain - copia usuarioId y descripción del request")
    void toDomain_requestValido_mapeaUsuarioYDescripcion() {
        // Arrange
        PerfilTrabajadorCreacionRequestDTO request = new PerfilTrabajadorCreacionRequestDTO();
        request.setUsuarioId(7L);
        request.setDescripcion("Plomero");

        // Act
        PerfilTrabajador dominio = mapper.toDomain(request);

        // Assert
        assertNotNull(dominio.getUsuario());
        assertEquals(7L, dominio.getUsuario().getId());
        assertEquals("Plomero", dominio.getDescripcion());
        assertNull(dominio.getId());
    }

    @Test
    @DisplayName("toResponse - expone usuarioId y no datos del usuario")
    void toResponse_dominioConUsuario_mapeaUsuarioId() {
        // Arrange
        PerfilTrabajador dominio = PerfilTrabajador.builder()
                .id(1L)
                .usuario(com.oficioya.model.domain.Usuario.builder().id(7L).contrasena("secreta").build())
                .zonaCobertura("Norte")
                .build();

        // Act
        PerfilTrabajadorResponseDTO response = mapper.toResponse(dominio);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals(7L, response.getUsuarioId());
        assertEquals("Norte", response.getZonaCobertura());
    }
}
