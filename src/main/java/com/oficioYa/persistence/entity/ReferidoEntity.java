package com.oficioya.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "referidos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referente_id", nullable = false)
    private UsuarioEntity referente;

    @Column(nullable = false)
    private String correoReferido;

    @Column(nullable = false)
    private boolean cuentaCreada;
    
    @Column(nullable = false)
    private LocalDateTime fechaInvitacion;
}
