package com.EcoBoost.PPI.repository;

import com.EcoBoost.PPI.entity.Sales;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends JpaRepository<Sales,Long> {
   @Query(value = "SELECT * FROM ventas WHERE usuario_id = :id", nativeQuery = true)
    List<Sales> findHistorialByUsuarioId(@Param("id") Long id);
}
