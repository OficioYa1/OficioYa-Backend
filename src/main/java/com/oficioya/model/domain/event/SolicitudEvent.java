package com.oficioYa.model.domain.event;

import com.oficioYa.model.domain.Solicitud;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SolicitudEvent {
    private Solicitud solicitud;
}
