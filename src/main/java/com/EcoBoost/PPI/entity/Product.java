package com.EcoBoost.PPI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="productos")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name="id_vendedor",nullable=true)
    private String documentoVendedor;

    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombre_producto;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = true)
    private String descripcion;

    @Column(name = "imagen_producto")
    private String imagenProducto;

    @Column(name = "cantidad_stock", nullable = false)
    private Integer cantidadStock;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Category categoria;

    // Relación OneToMany con CarritoCompras (a través de la tabla intermedia)
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carritos;
}

