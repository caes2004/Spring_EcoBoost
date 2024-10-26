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
    private Long id;

    @Column(nullable = false,  length = 60)
    private String nombre_producto;

    @Column(nullable = true, length = 60)
    private String descripcion;

    @Column(nullable = false,  length = 60)
    private float valor;

    @Column(nullable = true, length = 60)
    private String imagen;
    //Relacion de muchos con la entidad usuario
    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private User vendedor;

    @ManyToMany
    @JoinTable(
            name = "producto_categoria",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Category> categories;
}

