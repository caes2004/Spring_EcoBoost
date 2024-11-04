package com.EcoBoost.PPI.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="carrito")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long id;

    // Relación ManyToOne con Usuario (comprador)
    @ManyToOne
    @JoinColumn(name = "id_comprador", nullable = false)
    private User comprador;

    // Relación ManyToOne con Producto
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Product producto;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name = "valor_producto", nullable = false)
    private Double valorProducto;

    @Column(name = "cantidad_producto", nullable = false)
    private Integer cantidadProducto;

    @Column(name = "imagen_producto")
    private String imagenProducto;
}