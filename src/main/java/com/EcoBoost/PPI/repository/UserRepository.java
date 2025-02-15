package com.EcoBoost.PPI.repository;

import com.EcoBoost.PPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    User findByDocumento(String documento);

}
