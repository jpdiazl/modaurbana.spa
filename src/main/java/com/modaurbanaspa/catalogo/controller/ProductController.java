package com.modaurbanaspa.catalogo.controller;

import com.modaurbanaspa.catalogo.dto.ProductResponse;
import com.modaurbanaspa.catalogo.model.Product;
import com.modaurbanaspa.catalogo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/catalogo")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    // Crear nuevo producto
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody Product product) {
        ProductResponse newProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        try {
            ProductResponse product = productService.findById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id, 
            @RequestBody Product productDetails) {
        try {
            ProductResponse updatedProduct = productService.update(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductResponse>> getByCategoria(@PathVariable String categoria) {
        List<ProductResponse> products = productService.findByCategoria(categoria);
        return ResponseEntity.ok(products);
    }

    // Obtener productos destacados
    @GetMapping("/destacados")
    public ResponseEntity<List<ProductResponse>> getFeaturedProducts() {
        List<ProductResponse> products = productService.findDestacados();
        return ResponseEntity.ok(products);
    }

    // Obtener productos sostenibles
    @GetMapping("/sostenibles")
    public ResponseEntity<List<ProductResponse>> getSustainableProducts() {
        List<ProductResponse> products = productService.findSostenibles();
        return ResponseEntity.ok(products);
    }

    // Búsqueda simple
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String q) {
        List<ProductResponse> products = productService.search(q);
        return ResponseEntity.ok(products);
    }

    // Filtrar por precio
    @GetMapping("/precio")
    public ResponseEntity<List<ProductResponse>> filterByPrice(
            @RequestParam(defaultValue = "0") Double min,
            @RequestParam(defaultValue = "999999") Double max) {
        List<ProductResponse> products = productService.filterByPrice(min, max);
        return ResponseEntity.ok(products);
    }

    // Obtener estadísticas
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = productService.getEstadisticas();
        return ResponseEntity.ok(stats);
    }

    // Endpoint de salud
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = Map.of(
            "status", "UP",
            "service", "Catálogo de Productos",
            "timestamp", new java.util.Date().toString()
        );
        return ResponseEntity.ok(response);
    }
}