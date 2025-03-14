package com.EcoBoost.PPI.service;

import com.EcoBoost.PPI.entity.Cart;
import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.repository.CartRepository;
import com.EcoBoost.PPI.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RolServiceTest {

    @Mock
    private RolRepository rolRepository; //Mockeamos el repositorio para evitar acceder a la base de datos real.

    @InjectMocks
    private RolService rolService; //Mockeamos el servicio al que pertenece

    private Rol rol;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Aquí lo que haces es configurar un objeto de prueba para cada test
        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");
    }

    @Test
    void findByNombre() {

        when(rolRepository.findByNombre("ADMIN")).thenReturn(Optional.of(rol)); //✅ Simulamos que el repositorio devuelve el rol cuando se busca "ADMIN".

        Rol result = rolService.findByNombre("ADMIN"); //✅ Verificamos que se llame al mét0do findByNombre("ADMIN").

        assertNotNull(result); //✅ Comprobamos que el resultado no es null y tiene el nombre correcto.
        assertEquals("ADMIN", result.getNombre());
        verify(rolRepository).findByNombre("ADMIN");

    }

    @Test
    void findById() {

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        Rol result = rolService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(rolRepository).findById(1L);
    }

    @Test
    void save() {
        when(rolRepository.save(rol)).thenReturn(rol);

        Rol result = rolService.save(rol); //✅ Simulamos que el repositorio guarda el rol.


        assertNotNull(result);
        assertEquals("ADMIN", result.getNombre());
        verify(rolRepository).save(rol); //✅ Verificamos que el rol guardado tiene el mismo nombre.

    }

    @Test
    void listAll() {

        List<Rol> roles = Arrays.asList(rol, new Rol());
        when(rolRepository.findAll()).thenReturn(roles);

        List<Rol> result = rolService.listAll();

        assertEquals(2, result.size());
        verify(rolRepository).findAll();
    }
}