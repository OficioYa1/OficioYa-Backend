package com.oficioYa.service;

import com.oficioYa.model.domain.Usuario;

import com.oficioYa.model.domain.PerfilTrabajador;
import com.oficioYa.model.domain.PerfilContratante;

public interface IUsuarioService {
    Usuario registrarUsuario(Usuario usuario);
    void verificarCorreo(String correo);
    PerfilTrabajador obtenerPerfilTrabajador(Long usuarioId);
    /**
     * Obtiene el perfil público de un contratante por su ID de usuario (RF-53).
     * @param id Identificador único del usuario.
     * @return PerfilContratante de dominio con la información del perfil.
     */
    PerfilContratante obtenerPerfilContratante(Long id);

    /**
     * Edita la información básica de un trabajador (RF-57).
     */
    void editarPerfilTrabajador(Long usuarioId, String telefono, String zonaCobertura);

    /**
     * Edita la información básica de un contratante (RF-57).
     */
    void editarPerfilContratante(Long usuarioId, String telefono, String descripcion);

    /**
     * Da de baja lógicamente la cuenta de un usuario (RF-58).
     */
    void eliminarCuenta(Long usuarioId);

    /**
     * Sube y actualiza la foto de perfil del usuario (RF-59).
     */
    void actualizarFotoPerfil(Long usuarioId, org.springframework.web.multipart.MultipartFile archivo);

    /**
     * Inicia el flujo de recuperación de cuenta para un usuario (RF-75).
     */
    void recuperarCuenta(String correo);
}

