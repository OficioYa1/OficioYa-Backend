package com.oficioYa.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "oficios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OficioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; // "Plomería", "Electricidad", "Clases particulares", etc.

    @Column(nullable = false)
    private String categoria; // "Hogar", "Educación", "Mascotas", "Tecnología"

    @Column
    private String descripcion;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    // Custom constructor if needed outside builder
    public OficioEntity(String nombre, String categoria, String descripcion) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.activo = true;
    }
}
