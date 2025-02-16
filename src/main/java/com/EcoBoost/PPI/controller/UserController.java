package com.EcoBoost.PPI.controller;

import com.EcoBoost.PPI.entity.Category;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.service.CategoryService;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.RolService;
import com.EcoBoost.PPI.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private RolService rolService;
    @Autowired
    private CategoryService categoryService;



    public UserController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }


    @GetMapping("/path/admin")
    String mainAdmin(@RequestParam(value = "palabraClave", required = false) String palabraClave, Model model) {

        List<User> usuarios = userService.listAll();
        List<Product> productos = productService.listAll(palabraClave);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("productos", productos);
        return "mainAdmin";
    }

    //Eliminar productos y usuarios desde Administrador
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {

        userService.delete(id);
        return "redirect:/path/admin";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/path/admin";
    }
    //Editar productos y usuarios

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {

        User user = userService.get(id);
        model.addAttribute("user", user);

        List<Rol> roles = rolService.listAll();
        model.addAttribute("roles", roles);
        return "AdminEditUser";
    }

    @PostMapping("/editUser/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam("documento") String documento,
                             @RequestParam("nombre") String nombre,
                             @RequestParam("apellido") String apellido,
                             @RequestParam("password") String password,
                             @RequestParam("contacto") String contacto,
                             @RequestParam("correo") String correo,
                             @RequestParam("fechaNacimiento") LocalDate fechaNacimiento,
                             @RequestParam("direccion") String direccion,
                             @RequestParam("rolId") Long rolId) {
        User user = userService.get(id);
        Rol rol = rolService.findById(rolId);
        user.setDocumento(documento);
        user.setNombre(nombre);
        user.setApellido(apellido);
        user.setPassword(password);
        user.setContacto(contacto);
        user.setCorreo(correo);
        user.setFechaNacimiento(fechaNacimiento);
        user.setDireccion(direccion);
        user.setRol(rol);
        userService.save(user);
        return "redirect:/path/admin";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        Product producto = productService.findById(id);
        model.addAttribute("producto", producto);

        // Agregar categorías para seleccionar en el formulario
        List<Category> categorias = categoryService.listAll();
        model.addAttribute("categorias", categorias);

        return "AdminEditProduct";
    }

    @PostMapping("/editProduct/{id}")
    public String actualizarProducto(@PathVariable("id") Long id,
                                     @RequestParam("nombre_producto") String nombre,
                                     @RequestParam("descripcion") String descripcion,
                                     @RequestParam("valor") Double valor,
                                     @RequestParam("imagenProducto") MultipartFile imagen,
                                     @RequestParam("categoriaId") Long categoriaId,
                                     @RequestParam("cantidadStock") int cantidadStock) throws IOException {

        Product producto = productService.findById(id);
        Category category = categoryService.findById(categoriaId);

        producto.setNombre_producto(nombre);
        producto.setDescripcion(descripcion);
        producto.setCantidadStock(cantidadStock);
        producto.setValor(valor);
        producto.setCategoria(category);

        if (!imagen.isEmpty()) {
            String nombreImagen = imagen.getOriginalFilename();
            Path rutaImagen = Paths.get("src//main//resources//static/uploads", nombreImagen);
            Files.write(rutaImagen, imagen.getBytes());
            producto.setImagenProducto(nombreImagen);
        }

        productService.save(producto);

        return "redirect:/path/admin";
    }
    //Perfil usuario en sesión
    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        User user= (User) session.getAttribute("usuarioLogeado");
        if (user == null) {
            throw new RuntimeException("No hay un usuario logeado en la sesión");
        }
        model.addAttribute("user", user);
        return "perfil";
    }

    //Editar perfil de usuario en sesion

    @GetMapping("/editar")
    public String editar(HttpSession session, Model model) {

        User usuario = (User) session.getAttribute("usuarioLogeado");
        if (usuario == null) {
            throw new RuntimeException("No hay un usuario logeado en la sesión");
        }
        model.addAttribute("usuarioForm", usuario);
        return "editar_usuario";
    }

    @PostMapping("/editar")
    public String updateUser(@ModelAttribute User usuarioForm, HttpSession session) {
        User usuarioLogeado = (User) session.getAttribute("usuarioLogeado");


        usuarioLogeado.setPassword(usuarioForm.getPassword());
        usuarioLogeado.setDocumento(usuarioForm.getDocumento());
        usuarioLogeado.setNombre(usuarioForm.getNombre());
        usuarioLogeado.setApellido(usuarioForm.getApellido());
        usuarioLogeado.setContacto(usuarioForm.getContacto());
        usuarioLogeado.setCorreo(usuarioForm.getCorreo());
        usuarioLogeado.setFechaNacimiento(usuarioForm.getFechaNacimiento());
        usuarioLogeado.setDireccion(usuarioForm.getDireccion());

        userService.save(usuarioLogeado);

        String rol = usuarioLogeado.getRol().getNombre();
        return "comprador".equals(rol) ? "redirect:/comprador/home" : "redirect:/vendedor/home";
    }

}
