package com.EcoBoost.PPI.service.Email;

import com.EcoBoost.PPI.entity.User;

import jakarta.mail.MessagingException;

public interface EmailService {
    

    void enviarCorreoComprador(User user) throws MessagingException ;


}
