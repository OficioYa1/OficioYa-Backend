package com.oficioya.service.notification;

import com.oficioya.persistence.entity.UsuarioEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Estrategia de notificación por Correo Electrónico transaccional.
 */
@Slf4j
@Component
public class EmailNotificacionStrategy implements INotificacionStrategy {

    @Override
    public void enviarNotificacion(UsuarioEntity receptor, String titulo, String mensaje) {
        String correo = receptor != null ? receptor.getCorreo() : "sin-correo@oficioya.com";
        log.info("[NOTIFICACION-EMAIL] Enviando correo transaccional a <{}>. Asunto: '{}', Contenido: '{}'",
                correo, titulo, mensaje);
    }

    @Override
    public CanalNotificacion getCanal() {
        return CanalNotificacion.EMAIL;
    }
}
