package com.EcoBoost.PPI.controller;

import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    private final ProductService productService;
    private final UserService userService;

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

        return "mainAdmin";
    }
}
