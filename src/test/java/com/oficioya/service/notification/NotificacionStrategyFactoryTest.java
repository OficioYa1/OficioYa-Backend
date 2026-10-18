package com.oficioya.service.notification;

import com.oficioya.persistence.entity.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificacionStrategyFactoryTest {

    private NotificacionStrategyFactory factory;
    private PushNotificacionStrategy pushStrategy;
    private EmailNotificacionStrategy emailStrategy;
    private SmsNotificacionStrategy smsStrategy;

    @BeforeEach
    void setUp() {
        // Arrange
        pushStrategy = new PushNotificacionStrategy();
        emailStrategy = new EmailNotificacionStrategy();
        smsStrategy = new SmsNotificacionStrategy();
        factory = new NotificacionStrategyFactory(List.of(pushStrategy, emailStrategy, smsStrategy));
    }

    @Test
    @DisplayName("Debe resolver la estrategia Push correctamente")
    void debeResolverEstrategiaPush() {
        // Arrange
        CanalNotificacion canal = CanalNotificacion.PUSH;

        // Act
        INotificacionStrategy estrategia = factory.getStrategy(canal);

        // Assert
        assertNotNull(estrategia);
        assertEquals(CanalNotificacion.PUSH, estrategia.getCanal());
        assertTrue(estrategia instanceof PushNotificacionStrategy);
    }

    @Test
    @DisplayName("Debe resolver la estrategia Email correctamente")
    void debeResolverEstrategiaEmail() {
        // Arrange
        CanalNotificacion canal = CanalNotificacion.EMAIL;

        // Act
        INotificacionStrategy estrategia = factory.getStrategy(canal);

        // Assert
        assertNotNull(estrategia);
        assertEquals(CanalNotificacion.EMAIL, estrategia.getCanal());
        assertTrue(estrategia instanceof EmailNotificacionStrategy);
    }

    @Test
    @DisplayName("Debe resolver la estrategia SMS correctamente")
    void debeResolverEstrategiaSms() {
        // Arrange
        CanalNotificacion canal = CanalNotificacion.SMS;

        // Act
        INotificacionStrategy estrategia = factory.getStrategy(canal);

        // Assert
        assertNotNull(estrategia);
        assertEquals(CanalNotificacion.SMS, estrategia.getCanal());
        assertTrue(estrategia instanceof SmsNotificacionStrategy);
    }

    @Test
    @DisplayName("Debe retornar estrategia Push por defecto si el canal es nulo")
    void debeRetornarPushSiCanalEsNulo() {
        // Arrange
        CanalNotificacion canalNulo = null;

        // Act
        INotificacionStrategy estrategia = factory.getStrategy(canalNulo);

        // Assert
        assertNotNull(estrategia);
        assertEquals(CanalNotificacion.PUSH, estrategia.getCanal());
    }

    @Test
    @DisplayName("Debe ejecutar el envío de notificaciones sin lanzar excepciones")
    void debeEjecutarEnvioSinExcepciones() {
        // Arrange
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCorreo("test@oficioya.com");
        usuario.setTelefono("3001234567");

        // Act
        // Assert
        assertDoesNotThrow(() -> pushStrategy.enviarNotificacion(usuario, "Alerta", "Mensaje"));
        assertDoesNotThrow(() -> emailStrategy.enviarNotificacion(usuario, "Alerta", "Mensaje"));
        assertDoesNotThrow(() -> smsStrategy.enviarNotificacion(usuario, "Alerta", "Mensaje"));
        assertDoesNotThrow(() -> pushStrategy.enviarNotificacion(null, "Alerta", "Mensaje"));
    }
}
