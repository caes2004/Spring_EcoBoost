package com.EcoBoost.PPI.service.Email;
import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.EcoBoost.PPI.entity.Cart;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.service.CartService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private CartService cartService;

    @Override
    public void enviarCorreoComprador(User user) throws MessagingException {

    List<Cart> carts = cartService.listAll(user.getId());
    
    // Construcción del contenido en HTML
    StringBuilder htmlContent = new StringBuilder();
    htmlContent.append("<html><body>");
    htmlContent.append("<h2>Se realizó la compra exitosa de los siguientes productos:</h2>");
    htmlContent.append("<table border='1' style='border-collapse: collapse;'>");
    htmlContent.append("<tr><th>Producto</th><th>Cantidad</th><th>Precio unitario</th></tr>");

    double total=0;
    for (var carrito : carts) {
        htmlContent.append("<tr>")
            .append("<td>").append(carrito.getNombreProducto()).append("</td>")
            .append("<td>").append(carrito.getCantidadProducto()).append("</td>")
            .append("<td>").append(carrito.getValorProducto()).append("</td>")
            .append("</tr>");
            total+= carrito.getCantidadProducto()*carrito.getProducto().getValor();
    }
    
    htmlContent.append("</table>");
    htmlContent.append("<h1 style='color: #FF5733; font-size: 18px;'><strong>TOTAL: </strong></h1>").append("$").append(total);
    htmlContent.append("<p>¡Gracias por tu compra!</p>");
    htmlContent.append("<br/><br/>")
           .append("<p style='font-size: 14px; color: #888;'>")
           .append("<em>Este correo fue enviado de manera automatizada. Por favor, no responda a este correo.</em>")
           .append("</p>");
    htmlContent.append("</body></html>");

    
    String gmailDestino = user.getCorreo();
    String subject = "COMPRA EXITOSA ECOBOOST";

    // Crear el MimeMessage para correo en HTML
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true); // true para habilitar el soporte HTML

    helper.setFrom("ecoboostoficial@gmail.com");
    helper.setTo(gmailDestino);
    helper.setSubject(subject);
    helper.setText(htmlContent.toString(), true); // true indica que el contenido es HTML
    File logo = new File("src\\main\\resources\\static\\images\\login\\EcoBoost_Negativo.png");
    helper.addAttachment("ecoLogoOriginal.png", logo);
    
    emailSender.send(message);
}
}
