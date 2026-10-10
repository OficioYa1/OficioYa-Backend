package com.oficioYa.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "solicitudes")
@lombok.Getter
@lombok.Setter
@lombok.ToString(exclude = {"contratante", "trabajador"})
@lombok.EqualsAndHashCode(exclude = {"contratante", "trabajador"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** RF-19: Contratante que crea la solicitud */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contratante_id", nullable = false)
    private UsuarioEntity contratante;

    /** RF-23 / RF-26: Trabajador al que se le envió/aceptó */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajador_id")
    private UsuarioEntity trabajador;

    /** RF-19: Descripción del trabajo requerido */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    /** RF-21: Zona donde se requiere el servicio */
    @Column(name = "zona_servicio", nullable = false)
    private String zonaServicio;

    /** RF-20: URL de la foto adjunta a la solicitud */
    @Column(name = "foto_adjunta_url")
    private String fotoAdjuntaUrl;

    /** Máquina de estados del ciclo (RF-26, RF-78, RF-29, RF-76, RF-27) */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado = EstadoSolicitud.CREADA;

    @Builder.Default
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /**
     * Custom setter para actualizar automáticamente la fecha_actualizacion
     * al cambiar el estado.
     */
    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
        this.fechaActualizacion = LocalDateTime.now();
    }
}
