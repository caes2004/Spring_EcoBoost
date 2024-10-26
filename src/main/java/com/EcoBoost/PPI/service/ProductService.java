package com.EcoBoost.PPI.service;

import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired  ProductRepository productRepository;


    public List<Product> listAll(String palabraClave) {

        if (palabraClave != null){
            return productRepository.findAll( palabraClave);
        }

        return productRepository.findAll();
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public Product get (Long id) {

        return productRepository.findById(id).get();
    }

    public void delete (Long id) {

        productRepository.deleteById(id);
    }
}
