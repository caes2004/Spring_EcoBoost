package com.EcoBoost.PPI.unit;

import com.EcoBoost.PPI.entity.Category;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.repository.ProductRepository;
import com.EcoBoost.PPI.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension .class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setUp(){
        User user = new User();
        user.setId(1L);
        user.setNombre("Test");
        user.setDocumento("TestDocument");

        Category category = new Category();
        category.setId(1L);
        category.setNombre("TestCategory");

        product = new Product();
        product.setId(1L);
        product.setUsuario(user);
        product.setNombre_producto("TestProduct");
        product.setDescripcion("TestDescription");
        product.setValor(1.000);
        product.setCantidadStock(10);
        product.setImagenProducto("TestImage");
        product.setCategoria(category);

        System.out.println("----------SetUp: Producto inicializado----------");
    }
    @Test
    @DisplayName("Test listar todos los productos con palabra clave y sin palabra clave")
    public void testListAll(){
        String PalabraClave = "TestProduct";

        when(productRepository.findAll(PalabraClave)).thenReturn(List.of(product));
        System.out.println("----------Ejecutando test: Listar todos los productos con palabra clave----------");
        var products=productService.listAll(PalabraClave);
        assertNotNull(products);
        assertEquals(1,products.size());
        assertEquals(product.getNombre_producto(),products.getFirst().getNombre_producto());

        when(productRepository.findAll()).thenReturn(List.of(product));
        System.out.println("----------Ejecutando Test Listar todos los productos sin palabra clave----------");
        var products2=productService.listAll(null);
        assertNotNull(products2);
        assertEquals(1,products2.size());
        assertEquals(product.getNombre_producto(),products2.getFirst().getNombre_producto());

        verify(productRepository,times(1)).findAll(PalabraClave);
        verify(productRepository,times(1)).findAll();

        System.out.println("----------Test finalizado----------");
        System.out.println("Producto registrado: "+products.getFirst().getNombre_producto());

    }
    @Test
    @DisplayName("Test listar productos en landing page por categoria")
    public void testListProductDTOLanding(){

        when(productRepository.findByCategoriaId(1L)).thenReturn(List.of(product));
        System.out.println("----------Ejecutando Test: Listar productos en landing page por categoria----------");
        var products=productService.listProductsDTOLanding(1L);
        assertNotNull(products);
        assertEquals(1,products.size());
        assertEquals(product.getNombre_producto(),products.getFirst().getNombre());//<-Aca cambie el getNombre_producto por getNombre debido al cambio en el DTO

        when(productRepository.findAll()).thenReturn(List.of(product));
        System.out.println("----------Ejecutando Test: Listar productos en landing page sin categoria----------");
        var products2=productService.listProductsDTOLanding(null);
        assertNotNull(products2);
        assertEquals(1,products2.size());
        assertEquals(product.getNombre_producto(),products2.getFirst().getNombre());//<-Aca cambie el getNombre_producto por getNombre debido al cambio en el DTO

        verify(productRepository,times(1)).findByCategoriaId(1L);
        verify(productRepository,times(1)).findAll();

        System.out.println("----------Test finalizado----------");
        System.out.println("Producto registrado y filtrados con categoria: "+products.getFirst().getCategoria());
    }

    @Test
    @DisplayName("Test obtener producto por Documento vendedor")
    public void TestFindByDocumentoVendedor(){
        String documentoTest="Test";
        when(productRepository.findByDocumentoVendedor(documentoTest)).thenReturn(List.of(product));
        System.out.println("----------Ejecutando Test: Obtener producto por documento vendedor----------");
        var products=productService.findByDocumentoVendedor(documentoTest);

        verify(productRepository,times(1)).findByDocumentoVendedor(documentoTest);

        assertNotNull(products);
        assertEquals(1,products.size());
        assertEquals(product.getUsuario().getDocumento(),products.getFirst().getUsuario().getDocumento());
        System.out.println("----------Test finalizado----------");
        System.out.println("Producto obetenido con el documento del vendedor: "+products.getFirst().getUsuario().getDocumento());
    }

    @Test//Cuando en el servicio el metodo es void no se puede hacer un assert ademas del NotNull
    @DisplayName("Test: Guardar producto")
    public void TestSave(){
        Product productToSave = new Product();
        productToSave.setId(2L);
        productToSave.setNombre_producto("TestProductSave");
        productToSave.setValor(2.000);

        when(productRepository.save(productToSave)).thenReturn(productToSave);
        System.out.println("----------Ejecutando Test: Guardar producto----------");
        productService.save(productToSave);

        assertNotNull(productToSave);

        verify(productRepository,times(1)).save(productToSave);

        System.out.println("----------Test finalizado----------");
        System.out.println("Producto guardado: "+productToSave.getNombre_producto());

    }
    @Test
    @DisplayName("Test: Obtener producto por ID")
    public void testFindById(){
        Long id=1L;
        when(productRepository.findById(id)).thenReturn(java.util.Optional.of(product));
        System.out.println("----------Ejecutando Test: Obtener producto por ID----------");
        var result=productService.findById(id);

        assertNotNull(result);
        assertEquals(product.getNombre_producto(),result.getNombre_producto());
        assertEquals(product.getId(),result.getId());

        verify(productRepository,times(1)).findById(id);

        System.out.println("----------Test finalizado----------");
        System.out.println("Producto obtenido por ID: "+result.getNombre_producto());
    }
    @Test
    @DisplayName("Test: Eliminar producto")
    public void testDelete(){
        Long id=1L;
        System.out.println("----------Ejecutando Test: Eliminar producto----------");
        productService.delete(id);

        verify(productRepository,times(1)).deleteById(id);

        System.out.println("----------Test finalizado----------");
        System.out.println("Producto eliminado con ID: "+id);
    }
    @Test
    @DisplayName("Test: Intentar eliminar un producto que no existe")
    public void testDeleteNonExistingProduct() {

        Long id = 99L;
        when(productRepository.existsById(id)).thenReturn(false); // Simula que no existe
        System.out.println("----------Ejecutando Test: Intentar eliminar un producto que no existe----------");
        assertThrows(EntityNotFoundException.class, () -> productService.delete(id));
        verify(productRepository, never()).deleteById(id);

        System.out.println("----------Test finalizado----------");
        System.out.println("Intento de eliminar producto inexistente con ID: " + id);
    }
}
