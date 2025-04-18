package com.EcoBoost.PPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.EcoBoost.PPI.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE"
            + " CONCAT(p.id, p.nombre_producto,p.documentoVendedor, p.descripcion, p.valor)"
            + " LIKE %?1%")
    public List<Product> findAll(String palabraClave);

    List<Product> findByDocumentoVendedor(String documentoVendedor);

    
    List<Product>findByCategoriaId(Long categoriaId);

}
