package com.oficioya.validator.impl;

import com.oficioya.exception.ConflictoException;
import com.oficioya.exception.EstadoInvalidoException;
import com.oficioya.exception.UsuarioNoEncontradoException;
import com.oficioya.model.domain.FranjaDisponibilidad;
import com.oficioya.model.domain.PerfilTrabajador;
import com.oficioya.model.exception.ReglaDeNegocioException;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.UsuarioRepository;
import com.oficioya.validator.IPerfilTrabajadorValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PerfilTrabajadorValidatorImpl implements IPerfilTrabajadorValidator {

    private final UsuarioRepository usuarioRepository;
    private final PerfilTrabajadorRepository perfilRepository;

    @Override
    public void validarCuentaElegible(Long usuarioId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.warn("Creación de perfil: usuario inexistente id={}", usuarioId);
                    return new UsuarioNoEncontradoException("No se encontró ningún usuario con el ID: " + usuarioId);
                });

        if (usuario.getRol() != RolUsuario.TRABAJADOR) {
            log.warn("Creación de perfil: el usuario id={} no es TRABAJADOR (rol={})", usuarioId, usuario.getRol());
            throw new ReglaDeNegocioException("Solo un usuario con rol TRABAJADOR puede tener perfil de trabajador");
        }
        if (!usuario.isActivo()) {
            log.warn("Creación de perfil: el usuario id={} está inactivo", usuarioId);
            throw new ReglaDeNegocioException("La cuenta del usuario está inactiva");
        }
        if (!usuario.isCorreoVerificado() && !usuario.isTelefonoVerificado()) {
            log.warn("Creación de perfil: el usuario id={} no ha verificado correo ni teléfono", usuarioId);
            throw new ReglaDeNegocioException("La cuenta debe tener el correo o el teléfono verificado para crear el perfil");
        }
    }

    @Override
    public void validarPerfilNoExiste(Long usuarioId) {
        if (perfilRepository.existsByUsuarioId(usuarioId)) {
            log.warn("Creación de perfil: el usuario id={} ya tiene un perfil", usuarioId);
            throw new ConflictoException("El usuario " + usuarioId + " ya tiene un perfil de trabajador");
        }
    }

    @Override
    public void validarFranjasDisponibilidad(List<FranjaDisponibilidad> franjas) {
        for (FranjaDisponibilidad franja : franjas) {
            if (!franja.getHoraInicio().isBefore(franja.getHoraFin())) {
                log.warn("Franja inválida: dia={}, inicio={}, fin={}", franja.getDia(), franja.getHoraInicio(), franja.getHoraFin());
                throw new ReglaDeNegocioException("La hora de inicio debe ser anterior a la hora de fin (" + franja.getDia() + ")");
            }
        }

        Map<DayOfWeek, List<FranjaDisponibilidad>> porDia = franjas.stream()
                .collect(Collectors.groupingBy(FranjaDisponibilidad::getDia));

        for (Map.Entry<DayOfWeek, List<FranjaDisponibilidad>> entrada : porDia.entrySet()) {
            List<FranjaDisponibilidad> ordenadas = entrada.getValue().stream()
                    .sorted(Comparator.comparing(FranjaDisponibilidad::getHoraInicio))
                    .toList();
            for (int i = 1; i < ordenadas.size(); i++) {
                if (ordenadas.get(i).getHoraInicio().isBefore(ordenadas.get(i - 1).getHoraFin())) {
                    log.warn("Franjas solapadas el día {}", entrada.getKey());
                    throw new ReglaDeNegocioException("Hay franjas de disponibilidad solapadas el día " + entrada.getKey());
                }
            }
        }
    }

    @Override
    public void validarPuedeActivarDisponibleAhora(PerfilTrabajador perfil) {
        if (perfil.isDisponibleAhora()) {
            log.warn("Disponible ahora: el perfil id={} ya estaba activo", perfil.getId());
            throw new EstadoInvalidoException("El perfil ya está marcado como disponible ahora");
        }
        if (perfil.getZonaCobertura() == null || perfil.getZonaCobertura().isBlank()) {
            log.warn("Disponible ahora: el perfil id={} no tiene zona de cobertura", perfil.getId());
            throw new ReglaDeNegocioException("Define tu zona de cobertura antes de activar 'Disponible ahora'");
        }
    }

    @Override
    public void validarPuedeDesactivarDisponibleAhora(PerfilTrabajador perfil) {
        if (!perfil.isDisponibleAhora()) {
            log.warn("Disponible ahora: el perfil id={} ya estaba desactivado", perfil.getId());
            throw new EstadoInvalidoException("El perfil no está marcado como disponible ahora");
        }
    }
}
