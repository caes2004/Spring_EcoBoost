package com.EcoBoost.PPI.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    public List<Cart> listAllActiveCarts(Long idComprador){
        List<Cart>allCarts=cartRepository.findAllByCompradorId(idComprador);

        
        return allCarts.stream().filter(p->p.getActivo()==true).collect(Collectors.toList());
    }
    public List<Cart>listAllProductsBysSalesId(Long id){

        List<Cart>products=cartRepository.findAllBySalesId(id);

        return products;
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

