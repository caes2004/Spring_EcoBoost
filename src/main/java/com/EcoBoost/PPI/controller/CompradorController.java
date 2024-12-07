package com.EcoBoost.PPI.controller;

import com.EcoBoost.PPI.entity.Cart;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.service.CartService;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CompradorController {
    @Autowired
    ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @GetMapping("/comprador/home")String compradorHome(@RequestParam(value = "palabraClave", required = false) String palabraClave, Model model) {

        List<Product> productos = productService.listAll(palabraClave);

        // Agregar la lista de productos al modelo
        model.addAttribute("productos", productos);

        return "CompradorHome";
    }
    //Agregar producto al carrito
    @PostMapping("/comprador/agregar-al-carrito")
    public String agregarAlCarrito(@RequestParam("productoId") Long productoId,
                                   @RequestParam("cantidad") Integer cantidad,
                                   HttpSession session, RedirectAttributes redirectAttributes) {

        User comprador = (User) session.getAttribute("usuarioLogeado");


        Product producto = productService.findById(productoId);
        if (cantidad > producto.getCantidadStock()) {
            redirectAttributes.addFlashAttribute("error", "No puedes agregar más productos de los disponibles en stock.");
            return "redirect:/comprador/home";
        }

        Cart cartItem = new Cart();
        cartItem.setProducto(producto);
        cartItem.setComprador(comprador);  // Asignamos el comprador del HttpSession
        cartItem.setNombreProducto(producto.getNombre_producto());
        cartItem.setValorProducto(producto.getValor());
        cartItem.setCantidadProducto(cantidad);
        cartItem.setImagenProducto(producto.getImagenProducto());

        // Guardar en el carrito
        cartService.save(cartItem);

        redirectAttributes.addFlashAttribute("success", "Producto agregado al carrito correctamente.");
        // Redireccionar a la página principal del comprador
        return "redirect:/comprador/home";
    }

    @GetMapping("/comprador/carrito")
    public String verCarrito(HttpSession session, Model model) {
        User comprador = (User) session.getAttribute("usuarioLogeado");
        if (comprador == null) {
            return "redirect:/login";
        }

        List<Cart> carritos= cartService.listAll(comprador.getId());
        double granTotal = carritos.stream()
                .mapToDouble(cart -> cart.getProducto().getValor() * cart.getCantidadProducto())
                .sum();
        model.addAttribute("granTotal", granTotal);
        model.addAttribute("carritos", carritos);

        return "verCarrito";
    }

    @PostMapping("/carrito/eliminar/{id}")
    public String eliminarCarrito(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cartService.eliminarCarrito(id);
            redirectAttributes.addFlashAttribute("mensaje", "Carrito eliminado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un problema al intentar eliminar el carrito.");
        }
        return "redirect:/comprador/carrito"; // Redirige a la página del carrito
    }

    //Editar perfil de usuario en sesion

    @GetMapping("/editar")
    public String editar(HttpSession session, Model model) {

        User comprador = (User) session.getAttribute("usuarioLogeado");
        if (comprador == null) {
            throw new RuntimeException("No hay un usuario logeado en la sesión");
        }
        model.addAttribute("comprador", comprador);
        return "editar_usuario";
    }

    @PostMapping("/editar")
    public String updateUser(
                             @RequestParam("documento") String documento,
                             @RequestParam("nombre") String nombre,
                             @RequestParam("apellido") String apellido,
                             @RequestParam("password") String password,
                             @RequestParam("contacto") String contacto,
                             @RequestParam("correo") String correo,
                             @RequestParam("fechaNacimiento") LocalDate fechaNacimiento,
                             @RequestParam("direccion") String direccion,
                             HttpSession session) {
        User usuarioLogeado = (User) session.getAttribute("usuarioLogeado");

        usuarioLogeado.setDocumento(documento);
        usuarioLogeado.setNombre(nombre);
        usuarioLogeado.setApellido(apellido);
        usuarioLogeado.setPassword(password);
        usuarioLogeado.setContacto(contacto);
        usuarioLogeado.setCorreo(correo);
        usuarioLogeado.setFechaNacimiento(fechaNacimiento);
        usuarioLogeado.setDireccion(direccion);
        userService.save(usuarioLogeado);
        return "redirect:/comprador/home";
    }
}
