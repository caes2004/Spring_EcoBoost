package com.EcoBoost.PPI.service;

import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.repository.ProductRepository;
import com.EcoBoost.PPI.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;

    public List<User> listAll() {

        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User get(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id: " + id));
    }

    public User findUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByNombreAndPassword(username, password);
    }

    @Transactional
    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        for (Product product : user.getProductos()) {
            productService.delete(product.getId());
        }
        userRepository.delete(user);
    }
}