package com.EcoBoost.PPI.service;
import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre));
    }

    public Rol findById(Long id) {

        return rolRepository.findById(id).orElseThrow(() -> new RuntimeException("Rol no encontrado: " + id));
    }

    public Rol save(Rol rol) {

        return rolRepository.save(rol);
    }
}
