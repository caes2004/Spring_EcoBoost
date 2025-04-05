package com.EcoBoost.PPI.unit;

import com.EcoBoost.PPI.entity.Cart;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.Sales;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.repository.CartRepository;
import com.EcoBoost.PPI.repository.ProductRepository;
import com.EcoBoost.PPI.repository.SalesRepository;
import com.EcoBoost.PPI.repository.UserRepository;
import com.EcoBoost.PPI.service.SalesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalesServiceTest {
    @InjectMocks
    private SalesService salesService;

    @Mock
    private SalesRepository salesRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    private Sales sales;

    public Sales createSales(Long userId, String userName, String productName1, String productName2) {
        User userTest = new User();
        userTest.setId(userId);
        userTest.setNombre(userName);
        userTest.setEcoPoints(0);

        Product productTest1 = new Product();
        productTest1.setId(101L);
        productTest1.setNombre_producto(productName1);
        productTest1.setValor(50.0);
        productTest1.setCantidadStock(10);

        Product productTest2 = new Product();
        productTest2.setId(102L);
        productTest2.setNombre_producto(productName2);
        productTest2.setValor(50.0);
        productTest2.setCantidadStock(10);

        Cart cartTest1 = new Cart();
        cartTest1.setId(1L);
        cartTest1.setComprador(userTest);
        cartTest1.setNombreProducto(productTest1.getNombre_producto());
        cartTest1.setProducto(productTest1);
        cartTest1.setCantidadProducto(2);

        Cart cartTest2 = new Cart();
        cartTest2.setId(2L);
        cartTest2.setComprador(userTest);
        cartTest2.setNombreProducto(productTest2.getNombre_producto());
        cartTest2.setProducto(productTest2);
        cartTest2.setCantidadProducto(2);

        Sales sales = new Sales();
        sales.setUsuario(userTest);
        sales.setFecha(LocalDate.now());
        sales.setCarrito(List.of(cartTest1, cartTest2));
        sales.setTotal(200.0);

        return sales;
    }

    @BeforeEach
    public void setUp() {
        sales = createSales(1L, "TestUser", "Product1", "Product2");
        System.out.println("----------SetUp: Ventas Inicializado----------");
    }

    @Test
    @DisplayName("Test realizar Venta usuario y carrito no nulos")
    public void testRealizarVenta() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(sales.getUsuario()));
        when(cartRepository.findAllByCompradorId(userId)).thenReturn(sales.getCarrito());
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(salesRepository.save(any(Sales.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(cartRepository).deleteByCompradorId(userId);

        var resultado = salesService.realizarVenta(userId);

        assertNotNull(resultado);
        assertEquals(2, resultado.getCarrito().size());
        assertEquals(200.0, resultado.getTotal());
        assertEquals("TestUser", resultado.getUsuario().getNombre());
        assertEquals(userId, resultado.getUsuario().getId());

        verify(userRepository).findById(userId);
        verify(cartRepository).findAllByCompradorId(userId);
        verify(productRepository, times(2)).save(any(Product.class));
        verify(userRepository).save(any(User.class));
        verify(salesRepository).save(any(Sales.class));
        verify(cartRepository).deleteByCompradorId(userId);

        System.out.println("----------Test finalizado----------");
    }

    @Test
    @DisplayName("Test realizar Venta carrito vacio")
    public void testRealizarVentaCarritoVacio() {
        Long userId = 2L;
        User testUser = new User();//User con carrito vacio
        testUser.setId(userId);
        testUser.setNombre("TestUser");
        System.out.println("----------Ejecutando test: Carrito vacio----------");
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(cartRepository.findAllByCompradorId(userId)).thenReturn(List.of());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            salesService.realizarVenta(userId);
        });

        assertEquals("El carrito está vacío, no se puede realizar la compra", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(cartRepository).findAllByCompradorId(userId);
        System.out.println("----------Test finalizado----------");
    }

    @Test
    @DisplayName("Test realizar Venta usuario no encontrado")
    public void testRealizarVentaUsuarioNoEncontrado() {
        Long userId = 99L;
        System.out.println("----------Ejecutando test: Usuario no encontrado para la venta----------");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            salesService.realizarVenta(userId);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());

        verify(userRepository).findById(userId);
        System.out.println("----------Test finalizado----------");

    }
}
