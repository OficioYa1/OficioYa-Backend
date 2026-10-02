package com.oficioya.service;

import com.oficioya.model.dto.response.PerfilTrabajadorResponseDTO;
import com.oficioya.model.domain.FranjaDisponibilidad;
import com.oficioya.model.domain.MetodoPago;
import com.oficioya.model.domain.PerfilTrabajador;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/** Casos de uso de configuración del perfil de trabajador. Trabaja solo con objetos de dominio. */
public interface IPerfilTrabajadorService {

    /** RF-01: crea la ficha pública asociada a una cuenta verificada. */
    PerfilTrabajador crearPerfil(PerfilTrabajador perfil);

    /** RF-04: define la zona de cobertura. */
    PerfilTrabajador actualizarZonaCobertura(Long perfilId, String zonaCobertura);

    /** RF-05: registra la tarifa aproximada por hora. */
    PerfilTrabajador actualizarTarifa(Long perfilId, BigDecimal tarifaPorHora);

    /** RF-06: reemplaza la disponibilidad semanal. */
    PerfilTrabajador actualizarDisponibilidadSemanal(Long perfilId, List<FranjaDisponibilidad> franjas);

    /** RF-60: reemplaza los métodos de pago aceptados. */
    PerfilTrabajador actualizarMetodosPago(Long perfilId, Set<MetodoPago> metodosPago);

    /** RF-30: activa "Disponible ahora" para recibir solicitudes inmediatas. */
    PerfilTrabajador activarDisponibleAhora(Long perfilId);

    /** RF-31: desactiva "Disponible ahora" y retira al trabajador de la búsqueda inmediata. */
    PerfilTrabajador desactivarDisponibleAhora(Long perfilId);
    PerfilTrabajador registrarOficioPrincipal(Long perfilId, Long oficioId);
    PerfilTrabajador registrarOficiosSecundarios(Long perfilId, List<Long> oficiosIds);
    PerfilTrabajador actualizarDatosOperativos(Long perfilId, BigDecimal tarifaPorHora, String zonaCobertura);
}
