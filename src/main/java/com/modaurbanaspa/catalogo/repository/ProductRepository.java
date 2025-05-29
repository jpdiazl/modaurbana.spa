package com.modaurbanaspa.catalogo.repository;

import com.modaurbanaspa.catalogo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Solo métodos simples usando naming convention
    List<Product> findByActivoTrue();
    List<Product> findByCategoriaAndActivoTrue(String categoria);
    List<Product> findByDestacadoTrueAndActivoTrue();
    List<Product> findBySostenibleTrueAndActivoTrue();
}
