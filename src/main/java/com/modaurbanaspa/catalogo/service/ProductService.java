package com.modaurbanaspa.catalogo.service;

import com.modaurbanaspa.catalogo.dto.ProductResponse;
import com.modaurbanaspa.catalogo.model.Product;
import com.modaurbanaspa.catalogo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponse> findAll() {
        return productRepository.findByActivoTrue()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public ProductResponse save(Product product) {
        Product savedProduct = productRepository.save(product);
        return new ProductResponse(savedProduct);
    }

    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return new ProductResponse(product);
    }

    public ProductResponse update(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        product.setNombre(productDetails.getNombre());
        product.setCategoria(productDetails.getCategoria());
        product.setDescripcion(productDetails.getDescripcion());
        product.setPrecioActual(productDetails.getPrecioActual());
        product.setPrecioOriginal(productDetails.getPrecioOriginal());
        product.setTallas(productDetails.getTallas());
        product.setColores(productDetails.getColores());
        product.setStock(productDetails.getStock());
        product.setDestacado(productDetails.getDestacado());
        product.setSostenible(productDetails.getSostenible());
        product.setImagenUrl(productDetails.getImagenUrl());
        product.setEmprendedor(productDetails.getEmprendedor());
        
        Product updatedProduct = productRepository.save(product);
        return new ProductResponse(updatedProduct);
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        product.setActivo(false);
        productRepository.save(product);
    }

    public List<ProductResponse> findByCategoria(String categoria) {
        return productRepository.findByCategoriaAndActivoTrue(categoria)
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findDestacados() {
        return productRepository.findByDestacadoTrueAndActivoTrue()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findSostenibles() {
        return productRepository.findBySostenibleTrueAndActivoTrue()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    // Búsqueda simple en memoria
    public List<ProductResponse> search(String busqueda) {
        return productRepository.findByActivoTrue()
                .stream()
                .filter(p -> p.getNombre().toLowerCase().contains(busqueda.toLowerCase()) ||
                           p.getDescripcion().toLowerCase().contains(busqueda.toLowerCase()))
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    // Filtrado simple en memoria
    public List<ProductResponse> filterByPrice(Double minPrice, Double maxPrice) {
        return productRepository.findByActivoTrue()
                .stream()
                .filter(p -> p.getPrecioActual().doubleValue() >= minPrice && 
                           p.getPrecioActual().doubleValue() <= maxPrice)
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getEstadisticas() {
        List<Product> allProducts = productRepository.findByActivoTrue();
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalProductos", allProducts.size());
        stats.put("totalSostenibles", allProducts.stream().filter(Product::getSostenible).count());
        stats.put("totalDestacados", allProducts.stream().filter(Product::getDestacado).count());
        
        Set<String> emprendedores = allProducts.stream()
                .map(Product::getEmprendedor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        stats.put("totalEmprendedores", emprendedores.size());
        
        return stats;
    }
}
