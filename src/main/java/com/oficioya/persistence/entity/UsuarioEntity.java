package com.oficioya.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false, unique = true)
    private String telefono;

    @Column(nullable = false)
    private String contrasena; // Sin encriptar en Sprint 02 — JWT llega en Sprint 03

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol;

    @Column(name = "foto_perfil_url")
    private String fotoPerfil;  // RF-59

    @Builder.Default
    @Column(name = "correo_verificado", nullable = false)
    private boolean correoVerificado = false;  // RF-40

    @Builder.Default
    @Column(name = "telefono_verificado", nullable = false)
    private boolean telefonoVerificado = false;  // RF-40

    @Builder.Default
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private boolean activo = true;  // RF-58 (eliminar = desactivar)

    @Builder.Default
    @Column(name = "embajador", nullable = false)
    private boolean embajador = false; // RF-69 Insignia Referidos

    @Builder.Default
    @Column(name = "calificacion_promedio")
    private Double calificacionPromedio = 0.0; // RF-35 Calificar Contratante
}
