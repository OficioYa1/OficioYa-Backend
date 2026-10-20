package com.oficioya.model.domain.event;

import com.oficioya.model.domain.Solicitud;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SolicitudEvent {
    private Solicitud solicitud;
}
