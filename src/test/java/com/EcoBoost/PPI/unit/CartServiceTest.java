package com.EcoBoost.PPI.unit;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EcoBoost.PPI.entity.Cart;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.Sales;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.repository.CartRepository;
import com.EcoBoost.PPI.service.CartService;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    private Cart cart1;
    private Cart cart2;

    // Helper method to create a cart
    private Cart createCart(Long userId, String userName, String productName) {
        Product product = new Product();
        product.setNombre_producto(productName);

        Sales sales = new Sales();
        sales.setFecha(LocalDate.now());

        User user = new User();
        user.setId(userId);
        user.setNombre(userName);
        user.setDocumento("TestDocument");

        Cart cart = new Cart();
        cart.setComprador(user);
        cart.setProducto(product);
        cart.setSales(sales);
        cart.setNombreProducto(productName);
        cart.setValorProducto(100.0); 
        cart.setCantidadProducto(1); 
        cart.setImagenProducto("image.png"); 

        return cart;
    }

    @BeforeEach
    public void setUp() {
        cart1 = createCart(1L, "TestUserName1-Cart", "TestProductName1-Cart");
        cart2 = createCart(2L, "TestUserName2-Cart", "TestProductName2-Cart");

        System.out.println("----------SetUp: Carritos inicializados----------");
    }
    
    @Test
    @DisplayName("Test listar todos los carritos asociados a un comprador")
    public void TestListAllByIdComprador() {
        Long idParameter = 1L;
        when(cartRepository.findAllByCompradorId(idParameter)).thenReturn(List.of(cart1));
        System.out.println("----------Ejecutando test: Listar todos los carritos de un comprador----------");

        var carrito = cartService.listAll(idParameter);
    
        assertNotNull(carrito);
        assertEquals(1, carrito.size());
        assertEquals("TestUserName1-Cart", carrito.getFirst().getComprador().getNombre());
        
        
        verify(cartRepository,times(1)).findAllByCompradorId(idParameter);

        System.out.println("----------Test finalizado----------");
        System.out.println("Carrito asociado al comprador "+carrito.getFirst().getComprador().getNombre());
    }

    @Test
    @DisplayName("Test crear un nuevo carrito")
    public void TestSaveCart(){

        when(cartRepository.save(cart2)).thenReturn(cart2);
        System.out.println("----------Ejecutando test: Gurdar carrito----------");
        cartService.save(cart2);

        assertNotNull(cart2);

        verify(cartRepository,times(1)).save(cart2);
        System.out.println("----------Test finalizado----------");
        System.out.println("Carrito creado con id: "+cart2.getId()+" para el comprador "+cart2.getComprador().getNombre());

    }

    @Test
    @DisplayName("Test eliminar carrito")
    public void TestDeleteCart(){

        Long idParameter=1L;
        System.out.println("----------Ejecutando test: Eliminar carrito----------");
        when(cartRepository.existsById(idParameter)).thenReturn(true);
        cartService.eliminarCarrito(idParameter);

        verify(cartRepository,times(1)).deleteById(idParameter);
        verify(cartRepository,times(1)).existsById(idParameter);

        System.out.println("----------Test finalizado----------");
        System.out.println("Carrito eliminado con ID: "+idParameter);
    }
}
