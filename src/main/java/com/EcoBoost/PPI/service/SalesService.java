package com.EcoBoost.PPI.service;

import com.EcoBoost.PPI.entity.Cart;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.Sales;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.repository.CartRepository;
import com.EcoBoost.PPI.repository.ProductRepository;
import com.EcoBoost.PPI.repository.SalesRepository;
import com.EcoBoost.PPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalesService {
    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

        @Transactional
        public Sales realizarVenta(Long userID) {


            User user = userRepository.findById(userID)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));


            List<Cart> carritos = cartRepository.findAllByCompradorId(userID);


            if (carritos.isEmpty()) {
                throw new IllegalArgumentException("El carrito está vacío, no se puede realizar la compra");
            }


            Sales sales = new Sales();
            sales.setUsuario(user);
            sales.setFecha(LocalDate.now());


            double total = 0;
            for (Cart cart : carritos) {
                total += cart.getCantidadProducto() * cart.getProducto().getValor();
                cart.setSales(sales);


                Product producto = cart.getProducto();
                int cantidadProducto = cart.getCantidadProducto();


                if (producto.getCantidadStock() < cantidadProducto) {
                    throw new IllegalArgumentException("No hay suficiente stock para el producto: " + producto.getNombre_producto());
                }


                producto.setCantidadStock(producto.getCantidadStock() - cantidadProducto);


                productRepository.save(producto);
            }
            sales.setTotal(total);
            sales.setCarrito(carritos);
            salesRepository.save(sales);



            cartRepository.deleteByCompradorId(userID);



            return sales;
        }
    }

