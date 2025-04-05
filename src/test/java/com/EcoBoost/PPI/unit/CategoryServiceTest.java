package com.EcoBoost.PPI.unit;

import com.EcoBoost.PPI.entity.Category;
import com.EcoBoost.PPI.repository.CategoryRepository;
import com.EcoBoost.PPI.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    Category category1;
    Category category2;

    public Category createCategory(Long id, String nombre) {
        Category category = new Category();
        category.setId(id);
        category.setNombre(nombre);
        return category;
    }

    @BeforeEach
    public void setUp(){
        category1 = createCategory(1L, "Hogar");
        category2 = createCategory(2L, "Juguetes");
    }

    @Test
    @DisplayName("Test findByNombre Category existente")
    public void testFindByNombre(){
        String nombre = "Hogar";
        when(categoryRepository.findByNombre(nombre)).thenReturn(java.util.Optional.of(category1));
        System.out.println("----------Test findByNombre Category existente----------");

        var category = categoryService.findByCategoria(nombre);

        assertNotNull(category);
        assertEquals(category1, category);
        assertEquals(nombre,category.getNombre());

        verify(categoryRepository,atLeastOnce()).findByNombre(nombre);

        System.out.println("Category encontrado nombre: " + category.getNombre());
        System.out.println("Category encontrado Id: " + category.getId());
        System.out.println("----------Test finalizado----------");
    }

    @Test
    @DisplayName("Test FinByNombre Category no existente")
    public void testFindByNombreNoExist(){
        String nombre = "NoExistente";
        when(categoryRepository.findByNombre(nombre)).thenReturn(Optional.empty());
        System.out.println("----------Test findByNombre Category no existente----------");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoryService.findByCategoria(nombre);
        });

        assertEquals("Categoria no encontrada: " + nombre, exception.getMessage());

        verify(categoryRepository,atLeastOnce()).findByNombre(nombre);

        System.out.println("Category no encontrado: " + nombre);
        System.out.println("----------Test finalizado----------");
    }
    @Test
    @DisplayName("Test findById Category existente")
    public void testFindById(){
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category1));
        System.out.println("----------Test findById Category existente----------");

        var category = categoryService.findById(id);

        assertNotNull(category);
        assertEquals(category1, category);
        assertEquals(id,category.getId());

        verify(categoryRepository,atLeastOnce()).findById(id);

        System.out.println("Category encontrado nombre: " + category.getNombre());
        System.out.println("Category encontrado Id: " + category.getId());
        System.out.println("----------Test finalizado----------");
    }
    @Test
    @DisplayName("Test findById Category no existente")
    public void testFindByIdNoExist(){
        Long id = 99L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        System.out.println("----------Test findById Category no existente----------");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoryService.findById(id);
        });

        assertEquals("Categoria  no encontrada: " + id, exception.getMessage());

        verify(categoryRepository,atLeastOnce()).findById(id);

        System.out.println("Category no encontrado: " + id);
        System.out.println("----------Test finalizado----------");
    }
    @Test
    @DisplayName("Test save Category")
    public void testSave(){
        Category category3=createCategory(3L, "Accesorios");
        when(categoryRepository.save(category3)).thenReturn(category3);

        System.out.println("----------Test save Category----------");

        var category = categoryService.save(category3);

        assertNotNull(category);
        assertEquals(category3, category);
        assertEquals(category3.getNombre(),category.getNombre());

        verify(categoryRepository,atLeastOnce()).save(category3);

        System.out.println("Category guardada nombre: " + category.getNombre());
        System.out.println("Category guardada Id: " + category.getId());
        System.out.println("----------Test finalizado----------");
    }
    @Test
    @DisplayName("Test ListAll Category")
    public void testListAll(){
        System.out.println("----------Test ListAll Category----------");
        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        var category = categoryService.listAll();
        assertNotNull(category);
        assertEquals(2, category.size());
        assertEquals(category1, category.get(0));
        assertEquals(category2, category.get(1));
        assertEquals(category1.getNombre(), category.get(0).getNombre());
        assertEquals(category2.getNombre(), category.get(1).getNombre());
        assertEquals(category1.getId(), category.get(0).getId());
        assertEquals(category2.getId(), category.get(1).getId());
        verify(categoryRepository,atLeastOnce()).findAll();
        System.out.println("Categories encontrados nombres: " + category.get(0).getNombre() + ", " + category.get(1).getNombre());
        System.out.println("Categories encontrados Id's: " + category.get(0).getId() + ", " + category.get(1).getId());
        System.out.println("----------Test finalizado----------");

    }
    @Test
    @DisplayName("Test delete Category")
    public void testDelete(){
        String nombre = "Hogar";
        when(categoryRepository.findByNombre(nombre)).thenReturn(Optional.of(category1));
        System.out.println("----------Test delete Category----------");

        categoryService.delete(nombre);

        verify(categoryRepository,atLeastOnce()).delete(category1);
        System.out.println("Category eliminada nombre: " + category1.getNombre());
        System.out.println("Category eliminada Id: " + category1.getId());
        System.out.println("----------Test finalizado----------");
    }
}
