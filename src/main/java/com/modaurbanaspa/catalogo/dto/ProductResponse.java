package com.modaurbanaspa.catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private BigDecimal precioActual;
    private BigDecimal precioOriginal;
    private BigDecimal descuento;
    private List<String> tallas;
    private List<String> colores;
    private Integer stock;
    private Boolean destacado;
    private Boolean sostenible;
    private String imagenUrl;
    private String emprendedor;
    private Boolean activo;

    public ProductResponse(com.modaurbanaspa.catalogo.model.Product product) {
        this.id = product.getId();
        this.nombre = product.getNombre();
        this.categoria = product.getCategoria();
        this.descripcion = product.getDescripcion();
        this.precioActual = product.getPrecioActual();
        this.precioOriginal = product.getPrecioOriginal();
        this.descuento = product.getDescuento();
        this.tallas = product.getTallasList();
        this.colores = product.getColoresList();
        this.stock = product.getStock();
        this.destacado = product.getDestacado();
        this.sostenible = product.getSostenible();
        this.imagenUrl = product.getImagenUrl();
        this.emprendedor = product.getEmprendedor();
        this.activo = product.getActivo();
    }
}
