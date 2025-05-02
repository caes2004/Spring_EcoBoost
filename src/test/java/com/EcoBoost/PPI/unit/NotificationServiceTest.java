package com.EcoBoost.PPI.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.entity.event.Notification;
import com.EcoBoost.PPI.repository.NotificationRepository;
import com.EcoBoost.PPI.service.NotificationService;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @InjectMocks
    private NotificationService notiService;

    @Mock
    private NotificationRepository notiRepository;

    private Notification noti;

    @BeforeEach
    public void setUp(){

        Product product=new Product();
        product.setId(1L);
        product.setNombre_producto("Test Product");
        product.setImagenProducto("Test Imagen");

        User vendedor= new User();
        vendedor.setNombre("Test Vendedor");
        vendedor.setDocumento("Documento Test");

        noti= new Notification();
        noti.setId(1L);
        noti.setCantidad_vendida(10);
        noti.setFecha_venta(LocalDateTime.now());
        noti.setTotal(1.000);
        noti.setId_producto(product.getId());
        noti.setNombre_producto(product.getNombre_producto());
        noti.setImagen_producto(product.getImagenProducto());
        noti.setDocumentoVendedor(vendedor.getDocumento());
        
    }

   @Test
   @DisplayName("----------- Test Crear Notificación con valores correctos------------")
    public void testCreateNotificationValoresCorrectos() {
        Product product = new Product();
        product.setId(1L);
        product.setNombre_producto("Test Product");
        product.setImagenProducto("test.jpg");

        User vendedor = new User();
        vendedor.setDocumento("123456");

        notiService.createNotification(product, vendedor, 10, 100.0);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notiRepository).save(captor.capture());

        Notification noti = captor.getValue();
        assertEquals("123456", noti.getDocumentoVendedor());
        assertEquals(1L, noti.getId_producto());
        assertEquals("Test Product", noti.getNombre_producto());
        assertEquals(10, noti.getCantidad_vendida());
        assertEquals("test.jpg", noti.getImagen_producto());
        assertEquals(100.0, noti.getTotal());
        assertNotNull(noti.getFecha_venta());

        System.out.println("-----------Test Finalizado----------");
        System.out.println("Notificación Guardada"+noti);
    }

    @Test
    @DisplayName("-------Test Mostrar Notificacion por Vendedor")
    public void TestNotiByVendedor(){

        
        when(notiRepository.findAllByDocumentoVendedor("Documento Test"))
        .thenReturn(List.of(noti));

        List<Notification> expected= notiRepository.findAllByDocumentoVendedor("Documento Test");
        assertNotNull(expected);
        assertEquals("Documento Test", noti.getDocumentoVendedor());
        assertEquals("Test Product", noti.getNombre_producto());
        assertEquals(1l, noti.getId());

        verify(notiRepository,times(1)).findAllByDocumentoVendedor("Documento Test");

        System.out.println("--------------Test Finalizado---------------");
    }



}
