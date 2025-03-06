package com.EcoBoost.PPI.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeProductDTO {

    private String nombre;
    private String descripcion;
    private Double precio;
    private String categoria;
    private String imagen;
    private Integer cantidad;

}
