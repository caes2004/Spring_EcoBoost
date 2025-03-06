package com.EcoBoost.PPI.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EcoPointsUserDTO {
    private  String name;
    private  Integer ecoPoints;
    private  Double descuento;
    private  Integer compras;


}
