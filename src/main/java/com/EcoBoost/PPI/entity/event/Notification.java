package com.EcoBoost.PPI.entity.event;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="Notiticaciones")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long id_producto;

    @Column
    private String documentoVendedor;

    @Column
    private String nombre_producto;

    @Column
    private Integer cantidad_vendida;

    @Column
    private String imagen_producto;

    @Column
    private Double total;

    @Column
    private LocalDateTime fecha_venta;

    @Column
    private Boolean checked=false;

}
