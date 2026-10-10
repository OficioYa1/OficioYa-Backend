package com.oficioYa.model.dto.request;

import com.oficioYa.model.domain.MetodoPago;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

/** RF-60 */
@Data
public class MetodosPagoRequestDTO {

    @NotEmpty(message = "Debe indicar al menos un método de pago")
    private Set<MetodoPago> metodosPago;
}
