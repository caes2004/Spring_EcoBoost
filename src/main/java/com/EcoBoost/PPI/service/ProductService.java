package com.EcoBoost.PPI.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.EcoBoost.PPI.DTO.HomeProductDTO;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.repository.CategoryRepository;
import com.EcoBoost.PPI.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    //Listar por palabra clave del producto
    public List<Product> listAll(String palabraClave) {

        if (palabraClave != null){
            return productRepository.findAll( palabraClave);
        }
            List<Product> products = productRepository.findAll();
        return products.stream().filter(p->p.getCantidadStock()>0).toList();
    }
    public List<HomeProductDTO> listProductsDTOLanding(Long categoryId) {
        
        if(categoryId==null){
            List<Product> products = productRepository.findAll();
            return products.stream()
                    .filter(p->p.getCantidadStock()>0)
                    .map(product -> {
                HomeProductDTO homeProductDTO = new HomeProductDTO();
                homeProductDTO.setNombre(product.getNombre_producto());
                homeProductDTO.setDescripcion(product.getDescripcion());
                homeProductDTO.setPrecio(product.getValor());
                homeProductDTO.setCategoria(product.getCategoria().getNombre());
                homeProductDTO.setImagen(product.getImagenProducto());
                homeProductDTO.setCantidad(product.getCantidadStock());
                return homeProductDTO;
            }).toList();
        }
        List<Product>productsFilter=productRepository.findByCategoriaId(categoryId);
        return productsFilter.stream()
        .filter(p->p.getCantidadStock()>0)
        .map(product -> {
                HomeProductDTO homeProductDTO = new HomeProductDTO();
                homeProductDTO.setNombre(product.getNombre_producto());
                homeProductDTO.setDescripcion(product.getDescripcion());
                homeProductDTO.setPrecio(product.getValor());
                homeProductDTO.setCategoria(product.getCategoria().getNombre());
                homeProductDTO.setImagen(product.getImagenProducto());
                homeProductDTO.setCantidad(product.getCantidadStock());
                return homeProductDTO;
            }).toList();
    }
    //Listar por documento del vendedor
    public List<Product> findByDocumentoVendedor(String documentoVendedor) {
        return productRepository.findByDocumentoVendedor(documentoVendedor);
    }
    public void save(Product product) {
        productRepository.save(product);
    }
    //Buscar producto por ID

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }


    public Product get (Long id) {

        return productRepository.findById(id).get();
    }

    public void delete (Long id) {

        productRepository.deleteById(id);
    }
}
