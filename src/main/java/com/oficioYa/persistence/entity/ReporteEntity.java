package com.oficioYa.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportador_id", nullable = false)
    private UsuarioEntity reportador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportado_id", nullable = false)
    private UsuarioEntity reportado;

    @Column(nullable = false, length = 500)
    private String motivo;

    private String evidenciaUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReporte estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}
