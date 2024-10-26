package com.EcoBoost.PPI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompradorController {

    @GetMapping("/comprador/home")String compradorHome(){
        return "CompradorHome";
    }
}
