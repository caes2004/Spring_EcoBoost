package com.EcoBoost.PPI.config;

import com.EcoBoost.PPI.entity.Category;
import com.EcoBoost.PPI.entity.Product;
import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.entity.User;
import com.EcoBoost.PPI.repository.CategoryRepository;
import com.EcoBoost.PPI.repository.RolRepository;
import com.EcoBoost.PPI.repository.UserRepository;
import com.EcoBoost.PPI.service.ProductService;
import com.EcoBoost.PPI.service.RolService;
import com.EcoBoost.PPI.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RolRepository rolRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RolService rolService;
    private final UserService userService;
    private final ProductService productService;

    public DataInitializer(RolRepository rolRepository, CategoryRepository categoryRepository,
            UserRepository userRepository, RolService rolService, UserService userService,
            ProductService productService) {
        this.rolRepository = rolRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.rolService = rolService;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        rolInit();
        categroriasInit();
        userInit();

    }

    private void rolInit() {
        if (rolRepository.count() == 0) {
            List<Rol> roles = List.of(
                    new Rol("comprador"),
                    new Rol("vendedor"));
            rolRepository.saveAll(roles);
            System.out.println("Roles guardados en la base de datos");
        } else {

            System.out.println("Ya hay roles en la base de datos");
        }
    }

    private void categroriasInit() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category("Accesorios"),
                    new Category("Construcción"),
                    new Category("Juguetes"),
                    new Category("Hogar"));
            categoryRepository.saveAll(categories);
            System.out.println("Categorias guardadas en la base de datos");
        } else {

            System.out.println("Ya hay categorias en la base de datos");
        }
    }

    private void userInit() {
        if (userRepository.count() == 0) {
            Rol comprador = rolService.findByNombre("comprador");
            Rol vendedor = rolService.findByNombre("vendedor");
            Product producto = new Product();
            User user1 = new User(null, "000", "Juan", "Pérez", "000", "3001234567",
                    "juan@example.com", LocalDate.of(1990, 5, 20), "Calle 123",
                    100, comprador, null, null);

            User user2 = new User(null, "777", "Andres", "Cano", "777", "3001255557",
                    "andres@example.com", LocalDate.of(1990, 5, 20), "Calle 123",
                    60, comprador, null, null);

            // Inizializar producto
            producto.setId(null);
            producto.setNombre_producto("Prueba Producto");
            producto.setValor(20000.0);
            Category categoria = categoryRepository.findById(1L).orElseThrow();
            producto.setCategoria(categoria);
            producto.setCantidadStock(300);
            producto.setImagenProducto("productMetalLadyBird.png");
            producto.setDocumentoVendedor("111");
            producto.setDescripcion("Mariquitas reciclables");

            User user3 = new User(null, "111", "Vendedor", "Vendedor", "111", "3001848394",
                    "vendedor@example.com", LocalDate.of(1990, 5, 20), "Calle 123",
                    0, vendedor, null, List.of(producto));

            producto.setUsuario(user3);

            userService.save(user1);
            userService.save(user2);
            userService.save(user3);
            productService.save(producto);

            System.out.println("Usuarios genericos guardados en la base de datos y un producto asociado.");
        } else {
            System.out.println("Ya hay usuarios en la base de datos.");
        }
    }

}
