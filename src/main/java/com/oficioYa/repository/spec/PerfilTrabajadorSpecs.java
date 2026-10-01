package com.oficioya.repository.spec;

import com.oficioya.model.domain.CriteriosBusqueda;
import com.oficioya.persistence.entity.FranjaDisponibilidadEmbeddable;
import com.oficioya.persistence.entity.OficioEntity;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Filtros combinables de la búsqueda de trabajadores (Dev B). Cada método devuelve una
 * Specification independiente; {@link #desdeCriterios} las une con AND.
 */
public final class PerfilTrabajadorSpecs {

    private static final char ESCAPE = '\\';

    private PerfilTrabajadorSpecs() {
        // Clase utilitaria, no instanciable
    }

    /**
     * @param oficiosDelTexto IDs de oficios que coinciden con la descripción libre (RF-11)
     * @param terminosTexto   términos de la descripción libre (RF-11)
     */
    public static Specification<PerfilTrabajadorEntity> desdeCriterios(
            CriteriosBusqueda c, Collection<Long> oficiosDelTexto, List<String> terminosTexto) {

        Specification<PerfilTrabajadorEntity> spec = (root, query, cb) -> cb.conjunction();

        if (c.getOficioId() != null) {
            spec = spec.and(conOficio(c.getOficioId()));
        }
        if (tieneTexto(c.getCategoria())) {
            spec = spec.and(conCategoria(c.getCategoria()));
        }
        if (tieneTexto(c.getZona())) {
            spec = spec.and(zonaContiene(c.getZona()));
        }
        if (c.getTarifaMin() != null || c.getTarifaMax() != null) {
            spec = spec.and(tarifaEntre(c.getTarifaMin(), c.getTarifaMax()));
        }
        if (c.getDia() != null) {
            spec = spec.and(disponibleEn(c.getDia(), c.getHoraDesde(), c.getHoraHasta()));
        }
        if (c.isSoloDisponiblesAhora()) {
            spec = spec.and(disponibleAhora());
        }
        if (c.getCalificacionMinima() != null) {
            spec = spec.and(calificacionMinima(c.getCalificacionMinima()));
        }
        if (!terminosTexto.isEmpty()) {
            spec = spec.and(textoLibre(oficiosDelTexto, terminosTexto));
        }
        return spec;
    }

    /** RF-12: el trabajador ofrece ese oficio. */
    public static Specification<PerfilTrabajadorEntity> conOficio(Long oficioId) {
        return (root, query, cb) -> {
            marcarDistinct(query);
            Join<PerfilTrabajadorEntity, OficioEntity> oficio = root.join("oficios");
            return cb.equal(oficio.get("id"), oficioId);
        };
    }

    /** RF-12: el trabajador ofrece algún oficio de esa categoría. */
    public static Specification<PerfilTrabajadorEntity> conCategoria(String categoria) {
        return (root, query, cb) -> {
            marcarDistinct(query);
            Join<PerfilTrabajadorEntity, OficioEntity> oficio = root.join("oficios");
            return cb.equal(cb.lower(oficio.get("categoria")), categoria.trim().toLowerCase(Locale.ROOT));
        };
    }

    /** RF-13: la zona de cobertura del trabajador contiene la zona pedida. */
    public static Specification<PerfilTrabajadorEntity> zonaContiene(String zona) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("zonaCobertura")), patronContiene(zona), ESCAPE);
    }

    /** RF-14: tarifa por hora dentro del rango (los límites son opcionales). */
    public static Specification<PerfilTrabajadorEntity> tarifaEntre(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            List<Predicate> condiciones = new ArrayList<>();
            condiciones.add(cb.isNotNull(root.get("tarifaPorHora")));
            if (min != null) {
                condiciones.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("tarifaPorHora"), min));
            }
            if (max != null) {
                condiciones.add(cb.lessThanOrEqualTo(root.<BigDecimal>get("tarifaPorHora"), max));
            }
            return cb.and(condiciones.toArray(new Predicate[0]));
        };
    }

    /**
     * RF-15: el trabajador tiene una franja ese día que cubre la hora pedida.
     * Sin horas solo exige tener franja ese día; con solo horaDesde exige cubrir ese instante.
     */
    public static Specification<PerfilTrabajadorEntity> disponibleEn(DayOfWeek dia, LocalTime desde, LocalTime hasta) {
        return (root, query, cb) -> {
            marcarDistinct(query);
            Join<PerfilTrabajadorEntity, FranjaDisponibilidadEmbeddable> franja = root.join("disponibilidadSemanal");
            List<Predicate> condiciones = new ArrayList<>();
            condiciones.add(cb.equal(franja.get("dia"), dia));
            if (desde != null) {
                LocalTime fin = hasta != null ? hasta : desde;
                condiciones.add(cb.lessThanOrEqualTo(franja.<LocalTime>get("horaInicio"), desde));
                condiciones.add(cb.greaterThanOrEqualTo(franja.<LocalTime>get("horaFin"), fin));
            }
            return cb.and(condiciones.toArray(new Predicate[0]));
        };
    }

    /** RF-32: tiene "Disponible ahora" activo. */
    public static Specification<PerfilTrabajadorEntity> disponibleAhora() {
        return (root, query, cb) -> cb.isTrue(root.get("disponibleAhora"));
    }

    /** RF-16: calificación promedio mayor o igual al mínimo. */
    public static Specification<PerfilTrabajadorEntity> calificacionMinima(Double minima) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.<Double>get("calificacionPromedio"), minima);
    }

    /**
     * RF-11: ofrece alguno de los oficios que coinciden con el texto, o su descripción
     * personal contiene alguno de los términos.
     */
    public static Specification<PerfilTrabajadorEntity> textoLibre(Collection<Long> oficioIds, List<String> terminos) {
        return (root, query, cb) -> {
            marcarDistinct(query);
            List<Predicate> alternativas = new ArrayList<>();
            if (!oficioIds.isEmpty()) {
                Join<PerfilTrabajadorEntity, OficioEntity> oficio = root.join("oficios", JoinType.LEFT);
                alternativas.add(oficio.get("id").in(oficioIds));
            }
            for (String termino : terminos) {
                alternativas.add(cb.like(cb.lower(cb.coalesce(root.<String>get("descripcion"), "")),
                        patronContiene(termino), ESCAPE));
            }
            return cb.or(alternativas.toArray(new Predicate[0]));
        };
    }

    private static void marcarDistinct(jakarta.persistence.criteria.CriteriaQuery<?> query) {
        if (query != null) {
            query.distinct(true);
        }
    }

    private static boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }

    private static String patronContiene(String valor) {
        String escapado = valor.trim().toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + escapado + "%";
    }
}
