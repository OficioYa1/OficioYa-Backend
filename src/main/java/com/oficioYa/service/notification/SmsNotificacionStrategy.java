package com.oficioya.service.notification;

import com.oficioya.persistence.entity.UsuarioEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Estrategia de notificación por SMS (para solicitudes de urgencia / "Disponible Ahora").
 */
@Slf4j
@Component
public class SmsNotificacionStrategy implements INotificacionStrategy {

    @Override
    public void enviarNotificacion(UsuarioEntity receptor, String titulo, String mensaje) {
        String destino = receptor != null ? receptor.getTelefono() : "Móvil no registrado";
        log.info("[NOTIFICACION-SMS] Despachando mensaje de texto SMS urgente a {}: '{} - {}'",
                destino, titulo, mensaje);
    }

    @Override
    public CanalNotificacion getCanal() {
        return CanalNotificacion.SMS;
    }
}
