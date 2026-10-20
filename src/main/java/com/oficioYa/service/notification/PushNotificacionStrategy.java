package com.oficioya.service.notification;

import com.oficioya.persistence.entity.UsuarioEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Estrategia de notificación Push (simulación de Firebase Cloud Messaging).
 */
@Slf4j
@Component
public class PushNotificacionStrategy implements INotificacionStrategy {

    @Override
    public void enviarNotificacion(UsuarioEntity receptor, String titulo, String mensaje) {
        String receptorInfo = receptor != null ? receptor.getCorreo() : "Usuario Desconocido";
        log.info("[NOTIFICACION-PUSH] Disparando alerta Firebase FCM a '{}'. Título: '{}', Mensaje: '{}'",
                receptorInfo, titulo, mensaje);
    }

    @Override
    public CanalNotificacion getCanal() {
        return CanalNotificacion.PUSH;
    }
}
