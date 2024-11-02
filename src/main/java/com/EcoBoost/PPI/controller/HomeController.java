package com.EcoBoost.PPI.controller;

import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.service.RolService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
Controlador para manejo de endpoints publicos
 */
@Controller
public class HomeController {
@Autowired
ProductService productService;
@Autowired
private UserService userService;
@Autowired
private RolService rolService;
    @GetMapping("/") public String home(){

        //falta metodos para mostrar lista de productos en el carrusel
        return "home";
    }

    //Metodos para mostrar informacion de la empresa
    @GetMapping("/about") public String about(){


        return "about";
    }

    @GetMapping ("/contact")String contact(){

        return "contact";
    }

    //Metodos publico para mostrar formularios de registro y validacion de usuarios.

    @GetMapping("/create-user")String createUser(Model model){

        model.addAttribute("user", new User());
        return "create-user";
    }


    @PostMapping("/create-user")
    public String saveUser(User user, @RequestParam("rol") Long rolid) {
        // Buscar el rol basado en el userType
        Rol rol = rolService.findById(rolid);
        user.setRol(rol);

        userService.save(user); // Guarda el usuario en la base de datos

        return "redirect:/login"; // Redirige a la página principal después de guardar
    }
    @GetMapping("/login")String login(){

        return "login";
    }


    @PostMapping("/login")
    public String validateLogin(@RequestParam("userName") String username,
                                @RequestParam("password") String password,
                                Model model, HttpSession session) {
        User user = userService.findUserByUsernameAndPassword(username, password); // Usar el servicio
        ;
        if (user != null) {
            session.setAttribute("usuarioLogeado",user);
            // Verifica el rol del usuario y redirige a la página correspondiente
            if ("vendedor".equals(user.getRol().getNombre())) {

                return "redirect:/vendedor/home";
            } else if ("comprador".equals(user.getRol().getNombre())) {
                return "redirect:/comprador/home";


            } else {
                model.addAttribute("error", "Rol desconocido");
                return "pruebaError";
            }
        } else {
            // Si las credenciales son incorrectas, regresa al login con un mensaje de error
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "pruebaError";
        }
    }
}




