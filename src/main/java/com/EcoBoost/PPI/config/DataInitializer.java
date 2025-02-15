package com.EcoBoost.PPI.config;

import com.EcoBoost.PPI.entity.Category;
import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.repository.CategoryRepository;
import com.EcoBoost.PPI.repository.RolRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RolRepository rolRepository;
    private final CategoryRepository categoryRepository;


    public DataInitializer(RolRepository rolRepository, CategoryRepository categoryRepository) {
        this.rolRepository = rolRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        rolInit();
        categroriasInit();

    }
        private void rolInit(){
            if(rolRepository.count()==0){
                List<Rol> roles = List.of(
                        new Rol("comprador"),
                        new Rol("vendedor"));
                rolRepository.saveAll(roles);
                System.out.println("Roles guardados en la base de datos");
            }else{

                System.out.println("Ya hay roles en la base de datos");
            }
        }
        private void categroriasInit() {
            if (categoryRepository.count() == 0) {
                List<Category> categories = List.of(
                        new Category("Accesorios"),
                        new Category("Construcción"),
                        new Category("Juguetes"),
                        new Category("Hogar"));
                categoryRepository.saveAll(categories);
                System.out.println("Categorias guardadas en la base de datos");
            }else{

                System.out.println("Ya hay categorias en la base de datos");
            }
        }
}


