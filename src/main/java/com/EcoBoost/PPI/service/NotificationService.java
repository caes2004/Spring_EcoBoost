package com.EcoBoost.PPI.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.entity.event.Notification;
import com.EcoBoost.PPI.repository.NotificationRepository;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notiRepo;

    public void createNotification(Product producto,User vendedor,int cantidadProducto,Double totalUnitario){

        Notification noti=new Notification();
        noti.setDocumento_vendedor(vendedor.getDocumento());
        noti.setId_producto(producto.getId());
        noti.setNombre_producto(producto.getNombre_producto());
        noti.setCantidad_vendida(cantidadProducto);
        noti.setImagen_producto(producto.getImagenProducto());
        noti.setTotal(totalUnitario);
        noti.setFecha_venta(LocalDateTime.now());
        notiRepo.save(noti);
        
    }

    public List<Notification> notiByVendedor(User usuario){

        List<Notification> noti=notiRepo.findAll();
        
        return noti.stream()
        .filter(p -> p.getDocumento_vendedor().equals(usuario.getDocumento()))
        .collect(Collectors.toList());
    }

}
