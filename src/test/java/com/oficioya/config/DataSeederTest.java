package com.oficioya.config;

import com.oficioya.persistence.entity.OficioEntity;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.OficioRepository;
import com.oficioya.repository.PerfilContratanteRepository;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DataSeederTest {

    @Mock
    private OficioRepository oficioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PerfilTrabajadorRepository perfilTrabajadorRepository;

    @Mock
    private PerfilContratanteRepository perfilContratanteRepository;

    @Mock
    private Environment environment;

    private DataSeeder dataSeeder;

    @BeforeEach
    void setUp() {
        dataSeeder = new DataSeeder(
                oficioRepository,
                usuarioRepository,
                passwordEncoder,
                perfilTrabajadorRepository,
                perfilContratanteRepository,
                environment
        );
    }

    private List<OficioEntity> crearOficiosCatalogo() {
        return List.of(
                new OficioEntity("Plomería", "Emergencias", "Desc"),
                new OficioEntity("Electricidad", "Emergencias", "Desc"),
                new OficioEntity("Cerrajería", "Emergencias", "Desc"),
                new OficioEntity("Carpintería", "Obra", "Desc"),
                new OficioEntity("Pintura", "Obra", "Desc"),
                new OficioEntity("Jardinería", "Obra", "Desc"),
                new OficioEntity("Limpieza especializada", "Hogar", "Desc"),
                new OficioEntity("Soporte tecnológico", "Tec", "Desc")
        );
    }

    @Test
    @DisplayName("run - Ejecución completa con base de datos vacía y perfil local inicializa todo")
    void testRun_BaseVacia_PerfilLocal_InicializaTodo() throws Exception {
        // Arrange
        when(oficioRepository.count()).thenReturn(0L);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"local"});
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword123");

        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(inv -> {
            UsuarioEntity u = inv.getArgument(0);
            return UsuarioEntity.builder()
                    .id(1L)
                    .correo(u.getCorreo())
                    .telefono(u.getTelefono())
                    .nombre(u.getNombre())
                    .apellido(u.getApellido())
                    .contrasena(u.getContrasena())
                    .rol(u.getRol())
                    .activo(true)
                    .build();
        });

        when(perfilContratanteRepository.count()).thenReturn(0L);
        when(perfilTrabajadorRepository.count()).thenReturn(0L);
        when(oficioRepository.findAll()).thenReturn(crearOficiosCatalogo());

        // Act
        dataSeeder.run();

        // Assert
        verify(oficioRepository).saveAll(anyList());
        verify(usuarioRepository, atLeast(8)).save(any(UsuarioEntity.class));
        verify(perfilContratanteRepository).save(any());
        verify(perfilTrabajadorRepository, times(8)).save(any());
    }

    @Test
    @DisplayName("run - Con datos existentes no vuelve a sembrar")
    void testRun_ConDatosExistentes_NoDuplica() throws Exception {
        // Arrange
        when(oficioRepository.count()).thenReturn(18L);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"docker"});

        UsuarioEntity userMock = UsuarioEntity.builder()
                .id(2L)
                .correo("trabajador@oficioya.com")
                .rol(RolUsuario.TRABAJADOR)
                .build();
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.of(userMock));

        when(perfilContratanteRepository.count()).thenReturn(1L);
        when(perfilTrabajadorRepository.count()).thenReturn(8L);

        // Act
        dataSeeder.run();

        // Assert
        verify(oficioRepository, never()).saveAll(anyList());
        verify(usuarioRepository, never()).save(any());
        verify(perfilContratanteRepository, never()).save(any());
        verify(perfilTrabajadorRepository, never()).save(any());
    }

    @Test
    @DisplayName("run - Si perfil no es demo (ej. default sin flag) no carga perfiles de trabajadores")
    void testRun_PerfilNoDemo_OmitePerfiles() throws Exception {
        // Arrange
        when(oficioRepository.count()).thenReturn(18L);
        when(environment.getActiveProfiles()).thenReturn(new String[]{"default"});
        when(environment.getProperty("oficioya.seed.demo", "false")).thenReturn("false");

        // Act
        dataSeeder.run();

        // Assert
        verify(perfilTrabajadorRepository, never()).save(any());
        verify(perfilContratanteRepository, never()).save(any());
    }

    @Test
    @DisplayName("run - Manejo defensivo cuando repositorios opcionales son nulos")
    void testRun_CamposNulos_ManejoDefensivo() {
        // Arrange
        DataSeeder seederVacio = new DataSeeder(oficioRepository, null, null, null, null, null);
        when(oficioRepository.count()).thenReturn(18L);

        // Act & Assert
        assertDoesNotThrow(() -> seederVacio.run());
    }
}
