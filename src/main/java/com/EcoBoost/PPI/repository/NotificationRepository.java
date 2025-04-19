package com.EcoBoost.PPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EcoBoost.PPI.entity.event.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByDocumentoVendedor(String documentoVendedor);
}
