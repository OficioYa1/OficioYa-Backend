package com.oficioya.model.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int status,
        String error,
        String mensaje,
        String path,
        LocalDateTime timestamp
) {
    public ErrorResponseDTO(int status, String error, String mensaje, String path) {
        this(status, error, mensaje, path, LocalDateTime.now());
    }
}
