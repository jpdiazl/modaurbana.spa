package com.modaurbanaspa.catalogo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "CATEGORIA", nullable = false, length = 50)
    private String categoria;

    @Column(name = "DESCRIPCION", length = 500)
    private String descripcion;

    @Column(name = "PRECIO_ACTUAL", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioActual;

    @Column(name = "PRECIO_ORIGINAL", precision = 10, scale = 2)
    private BigDecimal precioOriginal;

    @Column(name = "DESCUENTO", precision = 5, scale = 2)
    private BigDecimal descuento;

    @Column(name = "TALLAS", length = 200)
    private String tallas;

    @Column(name = "COLORES", length = 200)
    private String colores;

    @Column(name = "STOCK", nullable = false)
    private Integer stock;

    @Column(name = "DESTACADO")
    private Boolean destacado = false;

    @Column(name = "SOSTENIBLE")
    private Boolean sostenible = true;

    @Column(name = "IMAGEN_URL", length = 500)
    private String imagenUrl;

    @Column(name = "EMPRENDEDOR", length = 100)
    private String emprendedor;

    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "ACTIVO")
    private Boolean activo = true;

    // Métodos auxiliares
    public List<String> getTallasList() {
        if (tallas == null || tallas.isEmpty()) {
            return List.of();
        }
        return List.of(tallas.split(","));
    }

    public List<String> getColoresList() {
        if (colores == null || colores.isEmpty()) {
            return List.of();
        }
        return List.of(colores.split(","));
    }

    @PrePersist
    @PreUpdate
    public void calcularDescuento() {
        if (precioOriginal != null && precioActual != null && precioOriginal.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal diferencia = precioOriginal.subtract(precioActual);
            this.descuento = diferencia.divide(precioOriginal, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        } else {
            this.descuento = BigDecimal.ZERO;
        }

        if (fechaCreacion == null) {
            fechaCreacion = new Date();
        }
    }
}
