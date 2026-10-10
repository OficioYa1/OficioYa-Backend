package com.oficioYa.model.domain;

import com.oficioYa.model.domain.state.*;
import com.oficioYa.persistence.entity.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {
    
    private Long id;
    private Usuario contratante;
    private Usuario trabajador;
    private String descripcion;
    private String zonaServicio;
    private String fotoAdjuntaUrl;
    private EstadoSolicitud estadoEnum;
    private SolicitudState estadoActual;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Configura el estado basado en el enum
    public void setEstadoEnum(EstadoSolicitud estado) {
        this.estadoEnum = estado;
        this.estadoActual = SolicitudStateFactory.getState(estado);
    }
    
    public void cambiarEstado(EstadoSolicitud nuevoEstado) {
        setEstadoEnum(nuevoEstado);
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public void enviar() { this.estadoActual.enviar(this); }
    public void aceptar() { this.estadoActual.aceptar(this); }
    public void rechazar() { this.estadoActual.rechazar(this); }
    public void iniciar() { this.estadoActual.iniciar(this); }
    public void completar() { this.estadoActual.completar(this); }
    public void cancelar() { this.estadoActual.cancelar(this); }
}
