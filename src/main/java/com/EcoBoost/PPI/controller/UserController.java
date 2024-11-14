package com.EcoBoost.PPI.controller;

import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.RolService;
import com.EcoBoost.PPI.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private  ProductService productService;
    @Autowired
    private  UserService userService;
    @Autowired
    private  RolService rolService;

    public UserController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping ("/logout")public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
    //Falta el security para este endpoint
    @GetMapping("/path/admin")String mainAdmin(@RequestParam(value = "palabraClave", required = false) String palabraClave, Model model) {

        List<User>usuarios= userService.listAll();
        List<Product>productos=productService.listAll(palabraClave);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("productos", productos);
        return "mainAdmin";
    }
    //Eliminar productos y usuarios.
    @GetMapping("/deleteUser/{id}")public String deleteUser(@PathVariable("id") Long id) {

        userService.delete(id);
        return "redirect:/path/admin";
    }
    @GetMapping("/deleteProduct/{id}") public String deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/path/admin";
    }
    //Editar productos y usuarios

    @GetMapping("/editUser/{id}") public String editUser(@PathVariable("id") Long id, Model model) {

        User user=userService.get(id);
        model.addAttribute("user", user);

        List<Rol>roles= rolService.listAll();
        model.addAttribute("roles", roles);
        return "AdminEditUser";
    }
    @PostMapping("/editUser/{id}")public String updateUser(@PathVariable("id") Long id,
                                                           @RequestParam("documento")String documento,
                                                           @RequestParam("nombre")String nombre,
                                                           @RequestParam("apellido")String apellido,
                                                           @RequestParam("password")String password,
                                                           @RequestParam("contacto")String contacto,
                                                           @RequestParam("correo")String correo,
                                                           @RequestParam("fechaNacimiento") LocalDate fechaNacimiento,
                                                           @RequestParam("direccion")String direccion,
                                                           @RequestParam("rolId")Long rolId) {
        User user=userService.get(id);
        Rol rol=rolService.findById(rolId);
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
}
