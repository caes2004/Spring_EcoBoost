package com.EcoBoost.PPI.service;

import com.EcoBoost.PPI.DTO.EcoPointsUserDTO;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {


    private final UserRepository userRepository;

    private final ProductService productService;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ProductService productService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productService = productService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listAll() {

        return userRepository.findAll();
    }
    public List<EcoPointsUserDTO> listUsersWithEcoPoints() {
        List <User> users = userRepository.findUsersWithEcoPoints();

        return users.stream().map(user -> {
            Double descuento = user.getEcoPoints() * 0.02;
            EcoPointsUserDTO ecoPointsUserDTO = new EcoPointsUserDTO();
            ecoPointsUserDTO.setName(user.getNombre());
            ecoPointsUserDTO.setEcoPoints(user.getEcoPoints());
            ecoPointsUserDTO.setDescuento(descuento);
            return ecoPointsUserDTO;
        }).toList();
    }

    public void save(User user) {
        User userExists = userRepository.findByDocumento(user.getDocumento());
        if (userExists != null) {  // Usuario ya existe
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(userExists.getPassword()); // Mantiene la contraseña anterior
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword())); // Encripta la nueva
            }
        } else {
            // Usuario nuevo, asegurarse de que la contraseña no sea null antes de encriptar
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("La contraseña no puede estar vacía.");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }

    public User get(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id: " + id));
    }

    public User authUser(String document, String password) {
        User user = userRepository.findByDocumento(document);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {

            return user;

        }else {
            return null;
        }
    }
    public Boolean recoveryPassword(String document , String email, String password){
        User user=userRepository.findByDocumento(document);
        if(user!=null&&user.getCorreo().equals(email)){
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
        return false;
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