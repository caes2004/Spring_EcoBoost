package com.EcoBoost.PPI.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.EcoBoost.PPI.entity.Category;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.entity.event.Notification;
import com.EcoBoost.PPI.service.CategoryService;
import com.EcoBoost.PPI.service.NotificationService;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller

public class VendedorController {
    @Autowired
    ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private NotificationService notiService;

    @GetMapping ("/vendedor/home")String vendedor_home(){

        return "VendedorHome";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@RequestParam("nombre_producto") String nombre,
                                  @RequestParam("descripcion") String descripcion,
                                  @RequestParam("valor") Double valor,
                                  @RequestParam("imagenProducto") MultipartFile imagen,
                                  @RequestParam("categoriaId") Long categoriaId,
                                  @RequestParam("cantidadStock") int cantidadStock,
                                  HttpSession session) throws IOException {

        // Obtener el usuario en sesión y su documento
        User usuarioLogeado = (User) session.getAttribute("usuarioLogeado");
        String documentoVendedor = usuarioLogeado.getDocumento();

        // Crear el producto con los datos del formulario
        Product producto = new Product();
        Category category = categoryService.findById(categoriaId);
        producto.setUsuario(usuarioLogeado);
        producto.setNombre_producto(nombre);
        producto.setDescripcion(descripcion);
        producto.setCantidadStock(cantidadStock);
        producto.setValor(valor);
        producto.setCategoria(category);
        producto.setDocumentoVendedor(documentoVendedor);

        // Guardar la imagen en el directorio usando Paths y Files.write
        if (!imagen.isEmpty()) {
            String nombreImagen = imagen.getOriginalFilename();
            Path rutaImagen = Paths.get("src//main//resources//static/uploads", nombreImagen);

            // Escribir el archivo en la ruta
            Files.write(rutaImagen, imagen.getBytes());

            // Solo almacenar el nombre de la imagen en la base de datos
            producto.setImagenProducto(nombreImagen);
        }

        // Guardar el producto en la base de datos
        productService.save(producto);

        return "redirect:/vendedor/home";
    }


    @GetMapping("/productos/mis-productos")
    public String verMisProductos(HttpSession session, Model model) {
        // Obtener el usuario en sesión
        User usuarioLogeado = (User) session.getAttribute("usuarioLogeado");
        String documentoVendedor = usuarioLogeado.getDocumento();

        // Obtener los productos del usuario en sesión
        List<Product> productos = productService.findByDocumentoVendedor(documentoVendedor);

        // Pasar los productos al modelo
        model.addAttribute("productos", productos);

        return "mis-productos";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable("id") Long id, Model model) {
        Product producto = productService.findById(id);
        model.addAttribute("producto", producto);

        // Agregar categorías para seleccionar en el formulario
        List<Category> categorias = categoryService.listAll();
        model.addAttribute("categorias", categorias);

        return "editar_producto";
    }

    // Guardar cambios en el producto editado
    @PostMapping("/editar/{id}")
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

        return "redirect:/productos/mis-productos";
    }
    @GetMapping("/vendedor/notificaciones")
    public String notificacionVendedor(HttpSession httpSession,Model model){
        User usuarioLogeado = (User) httpSession.getAttribute("usuarioLogeado");
        List<Notification> notificaciones=notiService.notiByVendedor(usuarioLogeado);
        model.addAttribute("notificaciones", notificaciones);
        return "notificaciones";
    }

    // Eliminar un producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/productos/mis-productos";
    }

}
