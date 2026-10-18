package com.oficioya.service.strategy;

import com.oficioya.persistence.entity.ResenaEntity;

import java.util.List;

/**
 * Patrón Strategy para el cálculo de la reputación de usuarios y oficios.
 * Permite intercambiar dinámicamente la fórmula matemática (promedio simple,
 * ponderado por antigüedad, etc.) sin acoplar la base de datos ni los servicios.
 */
public interface ICalculoReputacionStrategy {

    Double calcularPromedio(List<ResenaEntity> resenas);

    String getNombre();
}
