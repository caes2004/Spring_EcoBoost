package com.EcoBoost.PPI.repository;

import com.EcoBoost.PPI.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByCompradorId(Long idComprador);
    List<Cart> findAllByCompradorId(Long idComprador);
    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.comprador.id = :compradorId")
    void deleteByCompradorId(@Param("compradorId") Long compradorId);

}
