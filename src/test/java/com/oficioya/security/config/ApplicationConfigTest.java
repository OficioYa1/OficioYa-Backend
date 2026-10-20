package com.oficioya.security.config;

import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @Test
    @DisplayName("userDetailsService - Usuario existente retorna UserDetails activo con rol")
    void userDetailsService_UsuarioExiste() {
        String correo = "test@oficioya.com";
        UsuarioEntity usuario = UsuarioEntity.builder()
                .correo(correo)
                .contrasena("hashedPass")
                .activo(true)
                .rol(RolUsuario.TRABAJADOR)
                .build();

        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.of(usuario));

        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        UserDetails details = userDetailsService.loadUserByUsername(correo);

        assertNotNull(details);
        assertEquals(correo, details.getUsername());
        assertEquals("hashedPass", details.getPassword());
        assertTrue(details.isEnabled());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TRABAJADOR")));
    }

    @Test
    @DisplayName("userDetailsService - Usuario inexistente lanza UsernameNotFoundException")
    void userDetailsService_UsuarioInexistente_LanzaException() {
        String correo = "noexiste@oficioya.com";
        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.empty());

        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(correo));
    }

    @Test
    @DisplayName("authenticationProvider - Crea provider configurado correctamente")
    void authenticationProvider_CreaProvider() {
        AuthenticationProvider provider = applicationConfig.authenticationProvider();
        assertNotNull(provider);
    }

    @Test
    @DisplayName("authenticationManager - Obtiene manager desde configuration")
    void authenticationManager_ObtieneDesdeConfig() throws Exception {
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager result = applicationConfig.authenticationManager(authenticationConfiguration);

        assertNotNull(result);
        assertEquals(authenticationManager, result);
    }

    @Test
    @DisplayName("passwordEncoder - Retorna BCryptPasswordEncoder funcional")
    void passwordEncoder_RetornaBCrypt() {
        PasswordEncoder encoder = applicationConfig.passwordEncoder();
        assertNotNull(encoder);
        String raw = "password123";
        String encoded = encoder.encode(raw);
        assertTrue(encoder.matches(raw, encoded));
    }
}
