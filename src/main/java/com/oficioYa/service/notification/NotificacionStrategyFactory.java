package com.oficioya.service.notification;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Patrón Factory para resolver la estrategia de notificación en tiempo de ejecución.
 */
@Component
public class NotificacionStrategyFactory {

    private final Map<CanalNotificacion, INotificacionStrategy> estrategias = new EnumMap<>(CanalNotificacion.class);

    public NotificacionStrategyFactory(List<INotificacionStrategy> strategyList) {
        if (strategyList != null) {
            for (INotificacionStrategy strategy : strategyList) {
                estrategias.put(strategy.getCanal(), strategy);
            }
        }
    }

    public INotificacionStrategy getStrategy(CanalNotificacion canal) {
        if (canal == null) {
            return estrategias.get(CanalNotificacion.PUSH); // Canal por defecto
        }
        INotificacionStrategy strategy = estrategias.get(canal);
        if (strategy == null) {
            throw new IllegalArgumentException("No existe estrategia configurada para el canal: " + canal);
        }
        return strategy;
    }
}
