package com.oficioya.mapper;

import com.oficioya.model.domain.*;
import com.oficioya.model.dto.request.FiltroBusquedaDTO;
import com.oficioya.model.dto.request.SolicitudCreacionDTO;
import com.oficioya.model.dto.request.UsuarioRegistroRequestDTO;
import com.oficioya.model.dto.response.OficioResponseDTO;
import com.oficioya.model.dto.response.PerfilContratanteResponseDTO;
import com.oficioya.model.dto.response.SolicitudResponseDTO;
import com.oficioya.model.dto.response.UsuarioResponseDTO;
import com.oficioya.persistence.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MappersUnitTest {

    private final UsuarioEntityMapper usuarioEntityMapper = Mappers.getMapper(UsuarioEntityMapper.class);
    private final UsuarioDTOMapper usuarioDTOMapper = Mappers.getMapper(UsuarioDTOMapper.class);
    private final SolicitudMapper solicitudMapper = Mappers.getMapper(SolicitudMapper.class);
    private final SolicitudEntityMapper solicitudEntityMapper = Mappers.getMapper(SolicitudEntityMapper.class);
    private final PerfilContratanteMapper perfilContratanteMapper = Mappers.getMapper(PerfilContratanteMapper.class);
    private final PerfilContratanteEntityMapper perfilContratanteEntityMapper = Mappers.getMapper(PerfilContratanteEntityMapper.class);
    private final OficioMapper oficioMapper = Mappers.getMapper(OficioMapper.class);
    private final OficioEntityMapper oficioEntityMapper = Mappers.getMapper(OficioEntityMapper.class);
    private final BusquedaMapper busquedaMapper = Mappers.getMapper(BusquedaMapper.class);
    private final PerfilTrabajadorEntityMapper perfilTrabajadorEntityMapper = Mappers.getMapper(PerfilTrabajadorEntityMapper.class);

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        org.springframework.test.util.ReflectionTestUtils.setField(solicitudEntityMapper, "usuarioEntityMapper", usuarioEntityMapper);
        org.springframework.test.util.ReflectionTestUtils.setField(perfilContratanteEntityMapper, "usuarioEntityMapper", usuarioEntityMapper);
        org.springframework.test.util.ReflectionTestUtils.setField(perfilTrabajadorEntityMapper, "usuarioEntityMapper", usuarioEntityMapper);
        org.springframework.test.util.ReflectionTestUtils.setField(perfilTrabajadorEntityMapper, "oficioEntityMapper", oficioEntityMapper);
    }

    @Test
    @DisplayName("UsuarioEntityMapper: toEntity y toDomain con datos y nulos")
    void testUsuarioEntityMapper() {
        // Arrange
        assertNull(usuarioEntityMapper.toEntity(null));
        assertNull(usuarioEntityMapper.toDomain(null));

        Usuario domain = Usuario.builder()
                .id(1L).correo("test@test.com").telefono("1234567890")
                .nombre("Juan").apellido("Perez").rol(RolUsuario.CONTRATANTE)
                .activo(true).correoVerificado(true).telefonoVerificado(false)
                .fechaRegistro(LocalDateTime.now())
                .build();

        // Act
        UsuarioEntity entity = usuarioEntityMapper.toEntity(domain);
        Usuario mappedBack = usuarioEntityMapper.toDomain(entity);

        // Assert
        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getCorreo(), entity.getCorreo());
        assertNotNull(mappedBack);
        assertEquals(entity.getId(), mappedBack.getId());
    }

    @Test
    @DisplayName("UsuarioDTOMapper: toDomain y toResponseDTO con datos y nulos")
    void testUsuarioDTOMapper() {
        // Arrange
        assertNull(usuarioDTOMapper.toDomain(null));
        assertNull(usuarioDTOMapper.toResponseDTO(null));

        UsuarioRegistroRequestDTO req = new UsuarioRegistroRequestDTO(
                "c@test.com", "3001234567", "pass123", "Carlos", "Gomez", RolUsuario.TRABAJADOR
        );

        // Act
        Usuario domain = usuarioDTOMapper.toDomain(req);
        UsuarioResponseDTO res = usuarioDTOMapper.toResponseDTO(domain);

        // Assert
        assertNotNull(domain);
        assertEquals("c@test.com", domain.getCorreo());
        assertTrue(domain.isActivo());
        assertFalse(domain.isCorreoVerificado());
        assertNotNull(res);
        assertEquals(domain.getCorreo(), res.correo());
    }

    @Test
    @DisplayName("SolicitudMapper: toDomain y toResponse con datos y nulos")
    void testSolicitudMapper() {
        // Arrange
        assertNull(solicitudMapper.toDomain(null));
        assertNull(solicitudMapper.toResponse(null));

        SolicitudCreacionDTO dto = new SolicitudCreacionDTO();
        dto.setContratanteId(10L);
        dto.setDescripcion("Arreglo");
        dto.setZonaServicio("Norte");

        // Act
        Solicitud dom = solicitudMapper.toDomain(dto);

        // Assert
        assertNotNull(dom);
        assertEquals("Arreglo", dom.getDescripcion());

        // Arrange 2
        dom.setId(5L);
        dom.setContratante(Usuario.builder().id(10L).build());
        dom.setTrabajador(Usuario.builder().id(20L).build());
        dom.setEstadoEnum(EstadoSolicitud.CREADA);

        // Act 2
        SolicitudResponseDTO res = solicitudMapper.toResponse(dom);

        // Assert 2
        assertNotNull(res);
        assertEquals(5L, res.getId());
        assertEquals(10L, res.getContratanteId());
        assertEquals(20L, res.getTrabajadorId());
        assertEquals("CREADA", res.getEstado());

        // Solicitud sin contratante/trabajador
        Solicitud sinUsuarios = new Solicitud();
        SolicitudResponseDTO resSin = solicitudMapper.toResponse(sinUsuarios);
        assertNotNull(resSin);
        assertNull(resSin.getContratanteId());
    }

    @Test
    @DisplayName("SolicitudEntityMapper: toEntity y toDomain con datos y nulos")
    void testSolicitudEntityMapper() {
        // Arrange
        assertNull(solicitudEntityMapper.toEntity(null));
        assertNull(solicitudEntityMapper.toDomain(null));

        Solicitud domain = new Solicitud();
        domain.setId(1L);
        domain.setEstadoEnum(EstadoSolicitud.CREADA);
        domain.setContratante(Usuario.builder().id(2L).build());

        // Act
        SolicitudEntity entity = solicitudEntityMapper.toEntity(domain);
        Solicitud dom = solicitudEntityMapper.toDomain(entity);

        // Assert
        assertNotNull(entity);
        assertEquals(EstadoSolicitud.CREADA, entity.getEstado());
        assertNotNull(dom);
        assertEquals(EstadoSolicitud.CREADA, dom.getEstadoEnum());
    }

    @Test
    @DisplayName("PerfilContratanteMapper: toResponse con datos y nulos")
    void testPerfilContratanteMapper() {
        // Arrange
        assertNull(perfilContratanteMapper.toResponse(null));

        PerfilContratante domain = new PerfilContratante();
        domain.setId(1L);
        domain.setCalificacionPromedio(4.5);
        domain.setUsuario(Usuario.builder().id(9L).build());

        // Act
        PerfilContratanteResponseDTO res = perfilContratanteMapper.toResponse(domain);

        // Assert
        assertNotNull(res);
        assertEquals(9L, res.getUsuarioId());
        assertEquals(4.5, res.getCalificacionPromedio());

        PerfilContratante sinUsuario = new PerfilContratante();
        PerfilContratanteResponseDTO resSin = perfilContratanteMapper.toResponse(sinUsuario);
        assertNotNull(resSin);
        assertNull(resSin.getUsuarioId());
    }

    @Test
    @DisplayName("PerfilContratanteEntityMapper: toEntity y toDomain con datos y nulos")
    void testPerfilContratanteEntityMapper() {
        // Arrange
        assertNull(perfilContratanteEntityMapper.toEntity(null));
        assertNull(perfilContratanteEntityMapper.toDomain(null));

        PerfilContratante dom = new PerfilContratante();
        dom.setId(1L);
        dom.setDescripcion("Contratante puntual");
        dom.setUsuario(Usuario.builder().id(3L).build());

        // Act
        PerfilContratanteEntity entity = perfilContratanteEntityMapper.toEntity(dom);
        PerfilContratante mapped = perfilContratanteEntityMapper.toDomain(entity);

        // Assert
        assertNotNull(entity);
        assertEquals("Contratante puntual", entity.getDescripcion());
        assertNotNull(mapped);
        assertEquals("Contratante puntual", mapped.getDescripcion());
    }

    @Test
    @DisplayName("OficioMapper y OficioEntityMapper: toDto, toDtoList, toDomain, toEntity")
    void testOficioMappers() {
        // Arrange
        assertNull(oficioMapper.toDto(null));
        assertNull(oficioMapper.toDtoList(null));
        assertNull(oficioEntityMapper.toEntity(null));
        assertNull(oficioEntityMapper.toDomain(null));

        OficioEntity ofEntity = OficioEntity.builder().id(1L).nombre("Carpintería").descripcion("Madera").activo(true).build();
        Oficio ofDomain = Oficio.builder().id(1L).nombre("Carpintería").descripcion("Madera").activo(true).build();

        // Act
        OficioResponseDTO ofDto = oficioMapper.toDto(ofEntity);
        List<OficioResponseDTO> dtoList = oficioMapper.toDtoList(List.of(ofEntity));
        OficioEntity mappedEntity = oficioEntityMapper.toEntity(ofDomain);
        Oficio back = oficioEntityMapper.toDomain(mappedEntity);

        // Assert
        assertNotNull(ofDto);
        assertEquals("Carpintería", ofDto.getNombre());
        assertEquals(1, dtoList.size());
        assertNotNull(mappedEntity);
        assertEquals("Carpintería", mappedEntity.getNombre());
        assertNotNull(back);
        assertEquals("Carpintería", back.getNombre());
    }

    @Test
    @DisplayName("BusquedaMapper: toCriterios con datos y nulos")
    void testBusquedaMapper() {
        // Arrange
        assertNull(busquedaMapper.toCriterios(null));

        FiltroBusquedaDTO dto = new FiltroBusquedaDTO();
        dto.setTexto("plomero");
        dto.setZona("Chapinero");
        dto.setTarifaMax(new BigDecimal("50000"));

        // Act
        CriteriosBusqueda criterios = busquedaMapper.toCriterios(dto);

        // Assert
        assertNotNull(criterios);
        assertEquals("plomero", criterios.getTexto());
        assertEquals("Chapinero", criterios.getZona());
    }

    @Test
    @DisplayName("PerfilTrabajadorEntityMapper: toEntity y toDomain")
    void testPerfilTrabajadorEntityMapper() {
        // Arrange
        assertNull(perfilTrabajadorEntityMapper.toEntity(null));
        assertNull(perfilTrabajadorEntityMapper.toDomain(null));

        PerfilTrabajador pt = new PerfilTrabajador();
        pt.setId(10L);
        pt.setDescripcion("Electricista");
        pt.setTarifaPorHora(new BigDecimal("40000"));
        pt.setZonaCobertura("Suba");
        pt.setUsuario(Usuario.builder().id(5L).build());
        pt.setMetodosPago(Set.of(MetodoPago.EFECTIVO));
        pt.setFotosPortafolio(List.of("http://foto.jpg"));
        pt.setDisponibilidadSemanal(Collections.emptyList());

        // Act
        PerfilTrabajadorEntity entity = perfilTrabajadorEntityMapper.toEntity(pt);
        PerfilTrabajador back = perfilTrabajadorEntityMapper.toDomain(entity);

        // Assert
        assertNotNull(entity);
        assertEquals("Electricista", entity.getDescripcion());
        assertEquals("Suba", entity.getZonaCobertura());
        assertNotNull(back);
        assertEquals("Electricista", back.getDescripcion());
        assertEquals("Suba", back.getZonaCobertura());
    }
}
