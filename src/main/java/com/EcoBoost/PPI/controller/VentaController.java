package com.EcoBoost.PPI.controller;

import com.EcoBoost.PPI.entity.Sales;
import com.EcoBoost.PPI.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class VentaController {
    @Autowired
    SalesService salesService;

    @GetMapping("/factura/{userID}")
    public String factura(@PathVariable String userID, Model model) {
        try {
            Long id = Long.parseLong(userID);
            Sales sales = salesService.realizarVenta(id);
            model.addAttribute("sales", sales);
            return "factura";
        } catch (NumberFormatException e) {
            throw new RuntimeException("El valor proporcionado no es un número válido: " + userID);
        }
    }
}
