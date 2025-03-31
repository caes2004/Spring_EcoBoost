package com.EcoBoost.PPI.unit;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.repository.UserRepository;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.UserService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test unitarios de UserService")
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setDocumento("000");
        user.setCorreo("juan@example.com");
        user.setNombre("Juan");
        user.setApellido("Pérez");
        user.setPassword("password");
        System.out.println("----------SetUp: Usuario inicializado----------");
        System.out.println("Usuario: " + user.getNombre() + ", Documento: " + user.getDocumento());
    }

    @Test
    @DisplayName("Test: Listar todos los usuarios")
    public void testListAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        System.out.println("----------Ejecutando Test: Listar todos los usuarios----------");
        var users = userService.listAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Juan", users.get(0).getNombre());
        verify(userRepository, times(1)).findAll();

        System.out.println("Resultado de listar todos los usuarios: " + users);
    }

    @Test
    @DisplayName("Test: Obtener usuario por ID")
    public void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        System.out.println("----------Ejecutando Test: Obtener usuario por ID----------");
        var result = userService.get(1L);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("000", result.getDocumento());
        verify(userRepository, times(1)).findById(1L);

        System.out.println("Resultado de obtener usuario por ID: " + result);
    }

    @Test
    @DisplayName("Test: Guardar usuario nuevo")
    public void testSaveNewUser() {
        when(userRepository.findByDocumento(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("Contraseña Encriptada");
        when(userRepository.save(any(User.class))).thenReturn(user);

        user.setPassword("newPassword");

        System.out.println("----------Ejecutando Test: Guardar usuario nuevo----------");
        System.out.println("Antes de guardar usuario nuevo, contraseña: " + user.getPassword());
        userService.save(user);

        verify(userRepository, times(1)).save(user);
        System.out.println("Después de guardar usuario nuevo, contraseña: " + user.getPassword());
        assertEquals("Contraseña Encriptada", user.getPassword());
    }

    @Test
    @DisplayName("Test: Actualizar usuario existente")
    public void testUpdateUser() {
        when(userRepository.findByDocumento("000")).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("Contraseña Encriptada");
        when(userRepository.save(any(User.class))).thenReturn(user);

        user.setPassword("newPassword");

        System.out.println("----------Ejecutando Test: Actualizar usuario existente----------");
        System.out.println("Antes de actualizar usuario, contraseña: " + user.getPassword());
        userService.save(user);

        verify(userRepository, times(1)).save(user);
        System.out.println("Después de actualizar usuario, contraseña: " + user.getPassword());
        assertEquals("Contraseña Encriptada", user.getPassword());
    }

    @Test
    @DisplayName("Test: Autenticación de usuario")
    public void testAuthUser() {
        when(userRepository.findByDocumento("000")).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

        System.out.println("----------Ejecutando Test: Autenticación de usuario----------");
        var authenticatedUser = userService.authUser("000", "password");

        assertNotNull(authenticatedUser);
        assertEquals("Juan", authenticatedUser.getNombre());
        verify(userRepository, times(1)).findByDocumento("000");

        System.out.println("Resultado de autenticación de usuario: " + authenticatedUser);
    }

    @Test
    @DisplayName("Test: Recuperación de contraseña")
    public void testRecoveryPassword() {
        when(userRepository.findByDocumento("000")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("newPasswordEncoded");
        user.setPassword("oldPassword");

        System.out.println("----------Ejecutando Test: Recuperación de contraseña----------");
        System.out.println("Antes de la recuperación, contraseña: " + user.getPassword());
        boolean result = userService.recoveryPassword("000", "juan@example.com", "newPassword");

        System.out.println("Resultado de recuperación de contraseña: " + result);
        assertTrue(result);
        verify(userRepository, times(1)).save(user);
        assertEquals("newPasswordEncoded", user.getPassword());
        System.out.println("Después de la recuperación, contraseña: " + user.getPassword());
    }

    @Test
    @DisplayName("Test: Eliminar usuario")
    public void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(productService).delete(anyLong());
        doNothing().when(userRepository).delete(any(User.class));
        Product producto = new Product();
        producto.setId(1L);
        producto.setNombre_producto("Producto1");

        List<Product> productos = List.of(producto);
        user.setProductos(productos);

        System.out.println("----------Ejecutando Test: Eliminar usuario----------");
        System.out.println("Antes de eliminar usuario, productos asociados: " + user.getProductos());
        userService.delete(1L);

        verify(userRepository, times(1)).delete(user);
        verify(productService, times(1)).delete(anyLong());
        System.out.println("Usuario eliminado con éxito.");
    }

    @Test
    @DisplayName("Test: Error al intentar eliminar usuario no existente")
    public void testDeleteUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        System.out.println("----------Ejecutando Test: Error al intentar eliminar usuario no existente----------");
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.delete(1L);
        });

        System.out.println("Resultado de intentar eliminar un usuario no existente: " + exception.getMessage());

        assertEquals("Usuario no encontrado", exception.getMessage());
    }
}
