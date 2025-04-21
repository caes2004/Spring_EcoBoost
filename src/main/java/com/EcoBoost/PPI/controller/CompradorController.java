package com.EcoBoost.PPI.controller;

import com.EcoBoost.PPI.entity.Cart;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.Sales;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.service.CartService;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.SalesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
public class CompradorController {
    @Autowired
    ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private SalesService salesService;

    @GetMapping("/comprador/home")String compradorHome(@RequestParam(value = "palabraClave", required = false) String palabraClave, Model model,HttpSession session) {

        List<Product> productos = productService.listAll(palabraClave);
        User user= (User) session.getAttribute("user");
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

        List<Cart> carritos= cartService.listAllActiveCarts(comprador.getId());
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
    //Validar datos del comprador
    @GetMapping("/validar")
    public String validar(HttpSession session, Model model) {


        User usuario = (User) session.getAttribute("usuarioLogeado");
        if (usuario == null) {
            throw new RuntimeException("No hay un usuario logeado en la sesión");
        }
        model.addAttribute("usuario", usuario);
        return "validate";
    }

    @GetMapping("/comprador/historial")
    public String historial(HttpSession session, Model model) {
       
        User usuario=(User)session.getAttribute("usuarioLogeado");

        List<Sales>historialSales=salesService.historialCompras(usuario.getId());
        model.addAttribute("historial", historialSales);
        model.addAttribute("usuario", usuario);
        return  "historial";

    }
    @GetMapping("/comprador/historial/factura")
    public String historialFactura(@RequestParam ("compraId")long compraId,Model model,HttpSession session) {
        List<Cart> productos = cartService.listAllProductsBysSalesId(compraId);
        User user = (User) session.getAttribute("usuarioLogeado");

    int totalCompra = 0;
    for (Cart item : productos) {
        Product producto = item.getProducto(); 
        int cantidad = item.getCantidadProducto();     
        Double precio = producto.getValor();      
        totalCompra += cantidad * precio;
    }

        model.addAttribute("productos", productos);
        model.addAttribute("totalCompra", totalCompra);
        model.addAttribute("usuario", user);
        return "detalles-productos";
    }
    
    

}
