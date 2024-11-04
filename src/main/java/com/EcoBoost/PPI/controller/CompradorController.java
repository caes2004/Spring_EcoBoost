package com.EcoBoost.PPI.controller;

import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CompradorController {
    @Autowired
    ProductService productService;
    @GetMapping("/comprador/home")String compradorHome(@RequestParam(value = "palabraClave", required = false) String palabraClave, Model model) {
        // Llamar al servicio para obtener la lista de productos
        List<Product> productos = productService.listAll(palabraClave);

        // Agregar la lista de productos al modelo
        model.addAttribute("productos", productos);

        return "CompradorHome";
    }
}
