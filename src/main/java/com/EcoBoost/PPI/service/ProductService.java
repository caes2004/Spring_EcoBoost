package com.EcoBoost.PPI.service;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import com.EcoBoost.PPI.DTO.HomeProductDTO;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.repository.CategoryRepository;
import com.EcoBoost.PPI.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        List<Product> products = (categoryId == null)
                ? productRepository.findAll()
                : productRepository.findByCategoriaId(categoryId);

        return products.stream()
                .filter(p -> p.getCantidadStock() > 0)
                .map(this::convertToDTO)
                .toList();
    }

    private HomeProductDTO convertToDTO(Product product) {
        return new HomeProductDTO(
                product.getNombre_producto(),
                product.getDescripcion(),
                product.getValor(),
                product.getCategoria().getNombre(),
                product.getImagenProducto(),
                product.getCantidadStock()
        );
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


    //Evaluar eliminar este método
    public Product get (Long id) {

        return productRepository.findById(id).get();
    }

    public void delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + id);
        }
    }
}
