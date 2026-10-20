package com.oficioya.service.notification;

import com.oficioya.persistence.entity.UsuarioEntity;

/**
 * Patrón Strategy para envío de notificaciones.
 * Desacopla el canal de entrega de la lógica que dispara los eventos en el sistema.
 */
public interface INotificacionStrategy {

    void enviarNotificacion(UsuarioEntity receptor, String titulo, String mensaje);

    CanalNotificacion getCanal();
}
