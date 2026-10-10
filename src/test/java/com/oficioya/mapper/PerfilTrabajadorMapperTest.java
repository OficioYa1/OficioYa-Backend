package com.oficioYa.mapper;

import com.oficioYa.model.domain.FranjaDisponibilidad;
import com.oficioYa.model.domain.MetodoPago;
import com.oficioYa.model.domain.PerfilTrabajador;
import com.oficioYa.model.dto.request.FranjaDisponibilidadRequestDTO;
import com.oficioYa.model.dto.request.PerfilTrabajadorCreacionRequestDTO;
import com.oficioYa.model.dto.response.PerfilTrabajadorResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

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
                .usuario(com.oficioYa.model.domain.Usuario.builder().id(7L).contrasena("secreta").build())
                .zonaCobertura("Norte")
                .build();

        // Act
        PerfilTrabajadorResponseDTO response = mapper.toResponse(dominio);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals(7L, response.getUsuarioId());
        assertEquals("Norte", response.getZonaCobertura());
    }

    @Test
    @DisplayName("toFranjasDomain - convierte la lista de franjas del request al dominio")
    void toFranjasDomain_listaValida_mapeaCadaFranja() {
        // Arrange
        FranjaDisponibilidadRequestDTO franja = new FranjaDisponibilidadRequestDTO();
        franja.setDia(DayOfWeek.MONDAY);
        franja.setHoraInicio(LocalTime.of(8, 0));
        franja.setHoraFin(LocalTime.of(17, 0));

        // Act
        List<FranjaDisponibilidad> resultado = mapper.toFranjasDomain(List.of(franja));

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(DayOfWeek.MONDAY, resultado.get(0).getDia());
        assertEquals(LocalTime.of(8, 0), resultado.get(0).getHoraInicio());
        assertEquals(LocalTime.of(17, 0), resultado.get(0).getHoraFin());
    }

    @Test
    @DisplayName("toResponse - incluye franjas y métodos de pago")
    void toResponse_conFranjasYPagos_losIncluye() {
        // Arrange
        PerfilTrabajador dominio = PerfilTrabajador.builder()
                .id(1L)
                .usuario(com.oficioYa.model.domain.Usuario.builder().id(7L).build())
                .disponibilidadSemanal(List.of(FranjaDisponibilidad.builder()
                        .dia(DayOfWeek.TUESDAY).horaInicio(LocalTime.of(9, 0)).horaFin(LocalTime.of(12, 0)).build()))
                .metodosPago(Set.of(MetodoPago.NEQUI))
                .build();

        // Act
        PerfilTrabajadorResponseDTO response = mapper.toResponse(dominio);

        // Assert
        assertEquals(1, response.getDisponibilidadSemanal().size());
        assertEquals(DayOfWeek.TUESDAY, response.getDisponibilidadSemanal().get(0).getDia());
        assertEquals(Set.of(MetodoPago.NEQUI), response.getMetodosPago());
    }
}
