package com.EcoBoost.PPI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VendedorController {

    @GetMapping ("/vendedor/home")String vendedor_home(){

        return "VendedorHome";
    }

}
