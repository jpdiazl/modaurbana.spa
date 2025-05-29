package com.modaurbanaspa.catalogo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductFilter {
    private String categoria;
    private String talla;
    private String color;
    private BigDecimal precioMin;
    private BigDecimal precioMax;
    private Boolean destacado;
    private Boolean sostenible;
    private String busqueda;
    private String ordenarPor = "destacado"; // destacado, precio_asc, precio_desc, nuevo, popular
    private Integer pagina = 1;
    private Integer tamano = 6;
    private Boolean activo = true;
}