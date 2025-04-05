package com.EcoBoost.PPI.unit;

import com.EcoBoost.PPI.entity.Rol;
import com.EcoBoost.PPI.repository.RolRepository;
import com.EcoBoost.PPI.service.RolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {

    @InjectMocks
    private RolService rolService;

    @Mock
    private RolRepository rolRepository;

    private Rol Rol1;
    private Rol Rol2;

    public Rol createRole(Long id, String nombre) {
        Rol rol = new Rol();
        rol.setId(id);
        rol.setNombre(nombre);
        return rol;
    }
    @BeforeEach
    public void setUp() {
        Rol1= createRole(1L, "ROLE_VENDEDOR");
        Rol2= createRole(2L, "ROLE_COMPRADOR");
    }

    @Test
    @DisplayName("Test findByNombre Rol existente")
    public void testFindByNonmbre(){

        String nombre = "ROLE_VENDEDOR";
        when(rolRepository.findByNombre(nombre)).thenReturn(java.util.Optional.of(Rol1));
        System.out.println("----------Test findByNombre Rol existente----------");

        var rol = rolService.findByNombre(nombre);

        assertNotNull(rol);
        assertEquals(Rol1, rol);
        assertEquals(nombre,rol.getNombre());

        verify(rolRepository,atLeastOnce()).findByNombre(nombre);

        System.out.println("Rol encontrado nombre: " + rol.getNombre());
        System.out.println("Rol encontrado Id: " + rol.getId());
        System.out.println("----------Test finalizado----------");

    }

    @Test
    @DisplayName("Test findByNombre Rol no existente")
    public void testFindByNombreNoExistente(){
        String nombre="ROLE_NO_EXISTENTE";

        System.out.println("----------Test findByNombre Rol no existente----------");
        when(rolRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        Exception exception = assertThrows( RuntimeException.class,
                () -> rolService.findByNombre(nombre));
        assertEquals("Rol no encontrado: " + nombre, exception.getMessage());
        verify(rolRepository,atLeastOnce()).findByNombre(nombre);
        System.out.println("Rol no encontrado: " + nombre);
        System.out.println("----------Test findByNombre Rol no existente----------");
    }

    @Test
    @DisplayName("Test findById Rol existente")
    public void testFindById(){
        Long id = 2L;
        when(rolRepository.findById(id)).thenReturn(Optional.of(Rol2));
        System.out.println("----------Test findById Rol existente----------");

        var rol = rolService.findById(id);

        assertNotNull(rol);
        assertEquals(Rol2, rol);
        assertEquals(id,rol.getId());

        verify(rolRepository,atLeastOnce()).findById(id);

        System.out.println("Rol encontrado nombre: " + rol.getNombre());
        System.out.println("Rol encontrado Id: " + rol.getId());
        System.out.println("----------Test finalizado----------");
    }

    @Test
    @DisplayName("Test findById Rol no existente")
    public void testFindByIdNoExistente(){
        Long id = 99L;
        System.out.println("----------Test findById Rol no existente----------");
        when(rolRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows( RuntimeException.class,
                () -> rolService.findById(id));
        assertEquals("Rol no encontrado: " + id, exception.getMessage());
        verify(rolRepository,atLeastOnce()).findById(id);
        System.out.println("Rol no encontrado: " + id);
        System.out.println("----------Test findById Rol no existente----------");
    }
    @Test
    @DisplayName("Test save Rol")
    public void testSaveRol(){
        Rol Rol3 = createRole(3L, "ROLE_ADMIN");
        when(rolRepository.save(Rol3)).thenReturn(Rol3);
        System.out.println("----------Test save Rol----------");
        var rol = rolService.save(Rol3);
        assertNotNull(rol);
        assertEquals(Rol3, rol);
        assertEquals(Rol3.getId(), rol.getId());
        assertEquals(Rol3.getNombre(), rol.getNombre());
        verify(rolRepository,atLeastOnce()).save(Rol3);
        System.out.println("Rol guardado nombre: " + rol.getNombre());
        System.out.println("Rol guardado Id: " + rol.getId());
        System.out.println("----------Test finalizado----------");
    }

    @Test
    @DisplayName("Test listar todos los roles")
    public void testListAll(){
        when(rolRepository.findAll()).thenReturn(List.of(Rol1,Rol2));
        System.out.println("----------Test listar todos los roles----------");
        var rol = rolService.listAll();
        assertNotNull(rol);
        assertEquals(2, rol.size());
        verify(rolRepository,atLeastOnce()).findAll();
        System.out.println("Roles encontrados: " + rol);
        System.out.println("----------Test finalizado----------");
    }
    @Test
    @DisplayName("Test eliminar Rol")
    public void testDeleteRol(){
        String nombre = "ROLE_VENDEDOR";
        when(rolRepository.findByNombre(nombre)).thenReturn(Optional.of(Rol1));
        System.out.println("----------Test eliminar Rol----------");
        rolService.delete(nombre);
        verify(rolRepository,atLeastOnce()).delete(Rol1);
        System.out.println("Rol eliminado nombre: " + Rol1.getNombre());
        System.out.println("Rol eliminado Id: " + Rol1.getId());
        System.out.println("----------Test finalizado----------");
    }
}
