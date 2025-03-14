package com.EcoBoost.PPI.service;

import com.EcoBoost.PPI.entity.Cart;
import com.EcoBoost.PPI.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository; //Mockeamos el repositorio

    @InjectMocks
    private CartService cartService; //Mockeamos el servicio

    private Cart cart; //Definimos el tipo de listado de Cart para el primer método

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        cart = new Cart();
        cart.setId(1L);
    }

    @Test
    void listAll() {
        Long idComprador = 1L;
        when(cartRepository.findAllByCompradorId(idComprador)).thenReturn(Arrays.asList(cart));

        List<Cart> result = cartService.listAll(idComprador);

        assertEquals(1, result.size());
        verify(cartRepository).findAllByCompradorId(idComprador);

    }

    @Test //test para cuando el idComprador es nulo
    void listAll_SinIdComprador() {
        when(cartRepository.findAll()).thenReturn(Arrays.asList(cart));

        List<Cart> result = cartService.listAll(null);

        assertEquals(1, result.size());
        verify(cartRepository).findAll();
    }

    @Test
    void save() {
        cartService.save(cart);

        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void getCart() {

        CartRepository cartRepository = mock(CartRepository.class);
        CartService cartService = new CartService();
        cartService.cartRepository = cartRepository;  // Inyectamos el mock manualmente

        Long idComprador = 1L;
        Cart cart = new Cart(); // Se instancia un objeto Cart válido
        cart.setId(1L);

        when(cartRepository.findByCompradorId(idComprador)).thenReturn(cart);

        Cart result = cartService.getCart(idComprador);

        assertNotNull(result);  // Verifica que no sea null
        assertEquals(cart.getId(), result.getId()); // Verifica que el ID coincida
        verify(cartRepository).findByCompradorId(idComprador);
    }

    @Test
    void deleteCart() {

        Long idComprador = 1L;
        doNothing().when(cartRepository).deleteById(idComprador);

        cartService.deleteCart(idComprador);

        verify(cartRepository).deleteById(idComprador);
    }

    @Test
    void eliminarCarrito() {
        Long id = 1L;
        when(cartRepository.existsById(id)).thenReturn(true);
        doNothing().when(cartRepository).deleteById(id);

        cartService.eliminarCarrito(id);

        verify(cartRepository).existsById(id);
        verify(cartRepository).deleteById(id);
    }
}