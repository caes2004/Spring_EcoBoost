package com.EcoBoost.PPI.repository;

import com.EcoBoost.PPI.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByCompradorId(Long idComprador);
    List<Cart> findAllByCompradorId(Long idComprador);
}
