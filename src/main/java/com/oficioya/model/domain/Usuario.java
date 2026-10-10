package com.oficioYa.model.domain;

import com.oficioYa.persistence.entity.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    private Long id;
    private String correo;
    private String telefono;
    private String contrasena;
    private String nombre;
    private String apellido;
    private RolUsuario rol;
    private String fotoPerfil;
    private boolean correoVerificado;
    private boolean telefonoVerificado;
    private LocalDateTime fechaRegistro;
    private boolean activo;
    
}
