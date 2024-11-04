package com.EcoBoost.PPI.controller;

import org.springframework.ui.Model;

import com.EcoBoost.PPI.entity.Category;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.service.CategoryService;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller

public class VendedorController {
    @Autowired
    ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;

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

}
