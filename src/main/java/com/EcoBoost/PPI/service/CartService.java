package com.EcoBoost.PPI.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EcoBoost.PPI.entity.Cart;
import com.EcoBoost.PPI.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    public List<Cart> listAll(Long idComprador) {
        if (idComprador != null) {
            return cartRepository.findAllByCompradorId(idComprador);
        }
        return cartRepository.findAll();
    }

    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    public Cart getCart(Long idComprador) {
        return cartRepository.findByCompradorId(idComprador);
    }

    public void deleteCart(Long  id_comprador) {
        cartRepository.deleteById(id_comprador);
    }
    public void eliminarCarrito(Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("El carrito con el ID proporcionado no existe.");
        }
    }
}

