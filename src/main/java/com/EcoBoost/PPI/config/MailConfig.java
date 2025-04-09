package com.EcoBoost.PPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587); 
        mailSender.setUsername("ecoboostoficial@gmail.com"); 
        mailSender.setPassword("oeyfzcnswjbeeggv"); 

        // Propiedades adicionales para habilitar STARTTLS y autenticación
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Habilitar STARTTLS
        properties.put("mail.smtp.debug", "true"); 
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Asegura que se pueda establecer una conexión segura

        return mailSender;
    }
}
