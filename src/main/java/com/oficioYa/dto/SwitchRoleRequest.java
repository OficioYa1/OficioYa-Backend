package com.oficioYa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SwitchRoleRequest {
    private String newRole; // e.g., "TRABAJADOR", "CONTRATANTE"
}
