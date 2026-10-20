package com.oficioya.model.domain.decorator;

import java.util.List;

/**
 * Componente base del patrón Decorator para perfiles de trabajadores.
 * Define las operaciones visuales y de acreditación de méritos.
 */
public interface IPerfilVisualComponent {

    String getNombreVisible();

    List<String> getInsignias();

    boolean esAltamenteConfiable();

    String getBadgeDestacado();
}
