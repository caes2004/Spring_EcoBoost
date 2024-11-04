package com.EcoBoost.PPI.service;

import com.EcoBoost.PPI.entity.Category;
import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category findByCategoria(String categoria) {
        return categoryRepository.findByCategoria(categoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada: " + categoria));
    }

    public Category findById(Long id) {

        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria  no encontrada: " + id));
    }

    // Método para obtener todas las categorías
    public List<Category> listAll() {
        return categoryRepository.findAll();
    }
    public Category save(Category category) {

        return categoryRepository.save(category);
    }
}
