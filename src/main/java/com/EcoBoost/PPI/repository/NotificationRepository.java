package com.EcoBoost.PPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EcoBoost.PPI.entity.event.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
