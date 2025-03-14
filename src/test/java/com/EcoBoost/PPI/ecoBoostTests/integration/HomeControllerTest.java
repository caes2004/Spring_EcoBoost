package com.EcoBoost.PPI.ecoBoostTests.integration;

import com.EcoBoost.PPI.config.SecurityConfig;
import com.EcoBoost.PPI.controller.HomeController;
import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.repository.UserRepository;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.RolService;
import com.EcoBoost.PPI.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)//Para evitar problemas con los filtros de seguridad de Spring Security
public class HomeControllerTest {
    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserService userService;
    @MockBean
    private ProductService productService;
    @MockBean
    private RolService rolService;
    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Debe mostrar landing page con productos y usuarios con EcoPoints")
    public void testHome()throws Exception{
        //Simular los metodos de servicio
        when(productService.listProductsDTOLanding()).thenReturn(Collections.emptyList());
        when(userService.listUsersWithEcoPoints()).thenReturn(Collections.emptyList());
        //Validar la respuesta
        MvcResult result= mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("productos"))
                .andExpect(model().attributeExists("usuariosEcoPoints"))
                .andReturn();
        // Imprimir información de la respuesta
        MockHttpServletResponse response = result.getResponse();
        System.out.println("Status Code: " + response.getStatus());
        System.out.println("Contenido: " + response.getContentAsString());//<-- Aca muestra el HTML puro de la respuesta
    }
    @Test
    @DisplayName("Debe mostrar video de la EcoBoost al momento de dirigirse al Usuario ")
    public void testHomeVideo()throws Exception{
        MvcResult result= mockMvc.perform(get("/ecoVideo"))
                .andExpect(status().isOk())
                .andExpect(view().name("video"))
                .andReturn();
        // Imprimir información de la respuesta
        MockHttpServletResponse response = result.getResponse();
        System.out.println("Status Code: " + response.getStatus());
        System.out.println("Contenido: " + response.getContentAsString());//<-- Aca muestra el HTML puro de la respuesta
    }
    @Test
    @DisplayName("Debe mostrar formulario de login")
    public void testLogin()throws Exception{
        MvcResult result= mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andReturn();
        // Imprimir información de la respuesta
        MockHttpServletResponse response = result.getResponse();
        System.out.println("Status Code: " + response.getStatus());
        System.out.println("Contenido: " + response.getContentAsString());//<-- Aca muestra el HTML puro de la respuesta
    }
    @Test
    @DisplayName("Debe validar un usuario con credenciales validas y permitir el acceso")
    public void testLoginUser()throws Exception{
        // Simular un usuario existente en la BD
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);//<- Encriptar la contraseña
        User usuario = new User(
                1L,
                "123456789",
                "Juan",
                "Pérez",
                encodedPassword,
                "3123456789",
                "juan@example.com",
                LocalDate.of(1995, 5, 20),
                "Calle 123",
                0,
                new Rol(1L, "vendedor"),
                new ArrayList<>(),
                new ArrayList<>()
        );

        when(userRepository.findByDocumento(usuario.getDocumento())).thenReturn(usuario);

        doAnswer(p->{//Argumentos de acuerdo a la llamada en el controlador
            String inputdocument = p.getArgument(0);
            String inputpassword = p.getArgument(1);
            User userFound = userRepository.findByDocumento(inputdocument);//<-De acuerdo a AuthService se debe buscar el usuario por documento
            System.out.println("Autenticando usuario con documento:"+usuario.getDocumento()+" y contraseña:"+usuario.getPassword());
            if (userFound!=null &&
                    passwordEncoder.matches(inputpassword, userFound.getPassword())) {
                return userFound;
            }else{
                System.out.println(" Falló la autenticación para: " + inputdocument);
                throw new RuntimeException("Credenciales incorrectas");
            }
        }).when(userService).authUser(anyString(), anyString());

        MvcResult result= mockMvc.perform(post("/login")
                        .param("document", "123456789")
                        .param("password", "password123"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/vendedor/home"))
                        .andReturn();
        //  Verificar que `authUser` fue llamado correctamente
        verify(userService, times(1)).authUser(anyString(), anyString());
        // Imprimir información de la respuesta
        MockHttpServletResponse response = result.getResponse();
        System.out.println("Status Code: " + response.getStatus());
        //Asserts
        System.out.println(" Verificando redirección");
        assertEquals("/vendedor/home", response.getRedirectedUrl(), " Redirección incorrecta.");
        System.out.println(" Redirección correcta: " + response.getRedirectedUrl());

        System.out.println(" Verificando documento");
        assertEquals("123456789", usuario.getDocumento(), " Documento incorrecto.");
        System.out.println(" Documento correcto: " + usuario.getDocumento());

        System.out.println(" Verificando contraseña");
        assertTrue(passwordEncoder.matches(rawPassword, usuario.getPassword()), " La contraseña no coincide.");
        System.out.println(" Contraseña validada correctamente.");
    }
    @Test
    @DisplayName("Debe mostrar formulario de registro de usuario")
    public void testCreateUser()throws Exception{
       MvcResult result= mockMvc.perform(get("/create-user"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-user"))
                .andExpect(model().attributeExists("user"))
                .andReturn();
        // Imprimir información de la respuesta
        MockHttpServletResponse response = result.getResponse();
        System.out.println("Status Code: " + response.getStatus());
        System.out.println("Contenido: " + response.getContentAsString());//<-- Aca muestra el HTML puro de la respuesta
    }
    @Test
    @DisplayName("Debe guardar un usuario con rol USER y redirigir a /login")
    public void testSaveUser()throws Exception{
        // Simular un rol existente en la BD
        Long rolId = 1L;
        LocalDate date=LocalDate.now();
        Rol mockRol = new Rol();
        mockRol.setId(rolId);
        mockRol.setNombre("USER");
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);//<- Encriptar la contraseña

        //  Simular la respuesta de `rolService.findById()`
        when(rolService.findById(rolId)).thenAnswer(p->{
            System.out.println("Buscando rol con ID:"+rolId);
            return mockRol;
        });
        //Instanciar captura del usuario
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        //Simular la operación de guardado de usuario sin hacer nada
        //doNothing().when(userService).save(any(User.class));
        doAnswer(p->{
            User user = p.getArgument(0);
            System.out.println("Guardando usuario:"+user.getNombre());
            System.out.println("Password:"+user.getPassword());
            return null;
        }).when(userService).save((userCaptor.capture()));//<-Capturar el usuario que se guarda
        // Simular una petición `POST` con datos de usuario y rol
        MvcResult result= mockMvc.perform(post("/create-user")
                        .param("id", "1")
                        .param("documento", "123456789")
                        .param("nombre", "Juan")
                        .param("apellido", "Pérez")
                        .param("correo", "juan@example.com")
                        .param("direccion", "Calle 123")
                        .param("password",  "password123")
                        .param("fechaNacimiento", date.toString())
                        .param("rol", String.valueOf(rolId))) // Simula el envío del ID del rol
                .andExpect(status().is3xxRedirection()) //  Debe redirigir
                .andExpect(redirectedUrl("/login")).andReturn(); //  Debe redirigir a /login
        //  Verificar que `findById` y `save` fueron llamados correctamente
        verify(rolService, times(1)).findById(rolId);
        verify(userService, times(1)).save(any(User.class));
        User savedUser=userCaptor.getValue();
        System.out.println("Usuario guardado: " + savedUser.getNombre() + " | ID: " + savedUser.getId() +
                " | Rol: " + savedUser.getRol().getNombre() + " | Email: " + savedUser.getCorreo()+" | Password: "+encodedPassword);
        //  Verificar que el usuario fue guardado con el rol correcto
        assert(savedUser.getDocumento().equals("123456789"));
        assert(savedUser.getNombre().equals("Juan"));
        assert(passwordEncoder.matches(savedUser.getPassword(), encodedPassword));
        assert(savedUser.getRol().getId().equals(rolId));
        // Imprimir información de la respuesta
        MockHttpServletResponse response = result.getResponse();
        System.out.println("Status Code: " + response.getStatus());
        System.out.println("Redirección: " + response.getRedirectedUrl());
    }
    @Test
    @DisplayName("Debe mostrar formulario de recovery de contraseña")
    public void testRecovery()throws Exception{
        MvcResult result= mockMvc.perform(get("/recovery"))
                .andExpect(status().isOk())
                .andExpect(view().name("recovery"))
                .andReturn();
        // Imprimir información de la respuesta
        MockHttpServletResponse response = result.getResponse();
        System.out.println("Status Code: " + response.getStatus());
        System.out.println("Contenido: " + response.getContentAsString());//<-- Aca muestra el HTML puro de la respuesta
    }
    @Test
    @DisplayName("Debe recuperar la contraseña de un usuario con los datos validos cargados en la BD")
    public void testRecoveryPassword()throws Exception{
        //User simulado en la BD
        String doc = "123456789";
        String email = "juan@example.com";
        String newPassword = "newPassword";
        User mockUser = new User();
        mockUser.setDocumento(doc);
        mockUser.setCorreo(email);
        mockUser.setPassword("oldPassword");
        //simular recovery de password
        doAnswer(p->{
            String inputdocument = p.getArgument(0);
            String inputemail = p.getArgument(1);
            String inputpassword = p.getArgument(2);
            System.out.println("Recuperando contraseña para usuario con documento:"+mockUser.getDocumento()+" y contraseña:"+mockUser.getPassword());
             if (inputdocument.equals(mockUser.getDocumento()) && inputemail.equals(mockUser.getCorreo())) {
                mockUser.setPassword(inputpassword);  // Actualizar la contraseña
                return true;
            }
                return false;
        }).when(userService).recoveryPassword(anyString(), anyString(), anyString());

        MvcResult result= mockMvc.perform(post("/recovery")
                        .param("document", "123456789")
                        .param("email", "juan@example.com")
                        .param("password", "newPassword"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/recovery"))
                        .andExpect(flash().attributeExists("success"))
                        .andExpect(flash().attribute("success", "Contraseña actualizada con éxito"))
                        .andReturn();
        //  Verificar que `recoveryPassword` fue llamado correctamente
        verify(userService, times(1)).recoveryPassword(anyString(), anyString(), anyString());

        //  Verificar que la contraseña fue actualizada
        assertEquals(newPassword, mockUser.getPassword());
        // Imprimir información de la respuesta
        System.out.println("Contraseña actualizada para usuario con documento:"+mockUser.getDocumento()+" y contraseña:"+mockUser.getPassword());
        // Imprimir información de la respuesta
        MockHttpServletResponse response = result.getResponse();
        System.out.println("Status Code: " + response.getStatus());

    }
}
