package com.EcoBoost.PPI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="ventas")
@Data
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="usuario_id")
    private User usuario;

    @OneToMany(mappedBy = "sales",cascade=CascadeType.ALL,orphanRemoval = true)
    private List<Cart>carrito;

    private LocalDate fecha;

    private double total;
}
