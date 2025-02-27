package com.EcoBoost.PPI.repository;

import com.EcoBoost.PPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    User findByDocumento(String documento);
    @Query(value="SELECT * FROM usuarios WHERE rol_id = 1 and eco_points!=0 ORDER BY eco_points DESC", nativeQuery = true)
    List<User> findUsersWithEcoPoints();
}
