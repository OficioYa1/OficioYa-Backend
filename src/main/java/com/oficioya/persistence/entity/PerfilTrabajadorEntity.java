package com.oficioya.persistence.entity;

import com.oficioya.model.domain.MetodoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad de persistencia para el perfil técnico/operativo de un trabajador.
 *
 * ⚠️ OBJETO COMPARTIDO — DUEÑO: Desarrollador B (Worker Domain)
 * Esta entidad es usada por búsquedas (Dev B) y solicitudes (Dev C).
 * Antes de agregar un campo, comunicarlo al Desarrollador B.
 *
 * Cubre directamente: RF-01, RF-04, RF-05, RF-06, RF-09, RF-30, RF-31, RF-60
 * Es consultada por: RF-12-18 (búsqueda), RF-22 (matching en solicitudes)
 */
@Entity
@Table(name = "perfiles_trabajador")
@lombok.Getter
@lombok.Setter
@lombok.ToString(exclude = {"usuario", "oficios", "oficioPrincipal", "disponibilidadSemanal", "metodosPago"})
@lombok.EqualsAndHashCode(exclude = {"usuario", "oficios", "oficioPrincipal", "disponibilidadSemanal", "metodosPago"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilTrabajadorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Relación 1:1 con UsuarioEntity */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private UsuarioEntity usuario;

    /** RF-05: Tarifa aproximada por hora */
    @Column(name = "tarifa_hora", precision = 10, scale = 2)
    private BigDecimal tarifaPorHora;

    /** RF-04: Zona(s) de cobertura (ej: "Norte, Centro") */
    @Column(name = "zona_cobertura")
    private String zonaCobertura;

    /** RF-06: Disponibilidad semanal (día + franja horaria), tabla perfil_disponibilidad */
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "perfil_disponibilidad", joinColumns = @JoinColumn(name = "perfil_id"))
    private List<FranjaDisponibilidadEmbeddable> disponibilidadSemanal = new ArrayList<>();

    /** RF-30 / RF-31: Estado de disponibilidad inmediata */
    @Builder.Default
    @Column(name = "disponible_ahora", nullable = false)
    private boolean disponibleAhora = false;

    /** RF-60: Métodos de pago aceptados, tabla perfil_metodos_pago */
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "perfil_metodos_pago", joinColumns = @JoinColumn(name = "perfil_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private Set<MetodoPago> metodosPago = new HashSet<>();

    /** RF-09: Detalles específicos del oficio en formato libre */
    @Column(name = "detalles_especificos", columnDefinition = "TEXT")
    private String detallesEspecificos;

    /** Descripción libre del trabajador */
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /** Calificación promedio (se actualiza en Sprint 03 con reseñas) */
    @Builder.Default
    @Column(name = "calificacion_promedio")
    private Double calificacionPromedio = 0.0;

    /** RF-02: Oficio principal */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oficio_principal_id")
    private OficioEntity oficioPrincipal;


    /** Número total de trabajos completados */
    @Builder.Default
    @Column(name = "trabajos_completados", nullable = false)
    private int trabajosCompletados = 0;

    /** Oficios que ofrece el trabajador */
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "trabajador_oficios",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "oficio_id")
    )
    private List<OficioEntity> oficios = new ArrayList<>();
}
