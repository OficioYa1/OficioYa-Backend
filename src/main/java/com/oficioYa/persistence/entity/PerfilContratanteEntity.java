package com.oficioYa.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Entidad de persistencia para el perfil público de un contratante.
 */
@Entity
@Table(name = "perfiles_contratante")
@lombok.Getter
@lombok.Setter
@lombok.ToString(exclude = {"usuario"})
@lombok.EqualsAndHashCode(exclude = {"usuario"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilContratanteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private UsuarioEntity usuario;

    @Builder.Default
    @Column(name = "calificacion_promedio")
    private Double calificacionPromedio = 0.0;

    @Builder.Default
    @Column(name = "servicios_solicitados", nullable = false)
    private int serviciosSolicitados = 0;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
