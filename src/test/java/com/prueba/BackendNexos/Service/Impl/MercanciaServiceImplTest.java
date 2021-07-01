package com.prueba.BackendNexos.Service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prueba.BackendNexos.Models.Mercancia;
import com.prueba.BackendNexos.Models.Usuario;
import com.prueba.BackendNexos.Repository.MercanciaRepository;
import com.prueba.BackendNexos.exceptions.ExisteException;
import com.prueba.BackendNexos.exceptions.NoExisteException;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class MercanciaServiceImplTest {

  @Mock
  private static MercanciaRepository mercanciaRepository;

  @InjectMocks
  private static MercanciaServiceImpl mercanciaService;

  @Mock
  private static LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

  final static String YA_EXISTENTE = "Ya existe nombre";
  final static String NO_EXISTENTE = "existe nombre";
  private static Integer CANTIDAD = 10;

  private final static LocalDate FECHA_HORA_ACTUAL = LocalDate.now();

  @Test
  void test_crear_tipo_vehiculo() {
    when(mercanciaRepository.findByNombre(NO_EXISTENTE))
        .thenReturn(Optional.ofNullable(null));
    Mercancia mercancia = this.construirMercancia(0, NO_EXISTENTE);

    mercanciaService.crear(mercancia);

    verify(mercanciaRepository, times(1))
        .findByNombre(mercancia.getNombre());
    verify(mercanciaRepository, times(1))
        .save(mercancia);
  }

  @Test
  void test_nombre_ya_existe() {
    var mercancia = this.construirMercancia(0, YA_EXISTENTE);
    this.validarMercanciaExistente(YA_EXISTENTE);

    assertThrows(ExisteException.class,()->mercanciaService.crear(mercancia));

    verify(mercanciaRepository,times(1)).findByNombre(mercancia.getNombre());
    verify(mercanciaRepository,times(0)).save(mercancia);
  }

  @Test
  void cuando_actualiza_y_mercancia_no_existe_deberia_lanzar_exception() {
    var mercancia = this.construirMercancia(10, NO_EXISTENTE);
    when(mercanciaRepository.findById(mercancia.getId())).thenReturn(Optional.ofNullable(null));

    assertThrows(RuntimeException.class, ()-> mercanciaService.actualizar(mercancia));

    verify(mercanciaRepository, times(1))
        .findById(mercancia.getId());
    verify(mercanciaRepository, times(0))
        .save(mercancia);
  }

  @Test
  void cuando_actualiza_y_nombre_existe_deberia_lanzar_exception() {

    var mercancia = this.construirMercancia(0, YA_EXISTENTE);
    this.validarMercanciaExistente(YA_EXISTENTE);

    assertThrows(ExisteException.class,()->mercanciaService.actualizar(mercancia));

    verify(mercanciaRepository,times(1)).findByNombre(mercancia.getNombre());
    verify(mercanciaRepository,times(0)).save(mercancia);

  }

  @Test
  void cuando_borrar_mercancia_existe() {
    var mercancia = this.construirMercancia(20, YA_EXISTENTE);
    Optional<Mercancia> mercanciaExistente = Optional.of(mercancia);

    when(mercanciaRepository.findById(20)).thenReturn(mercanciaExistente);

    mercanciaService.borrar(20,1);
    verify(mercanciaRepository, times(1)).deleteById(20);
  }

  @Test
  void cuando_borrar_mercancia_no_existe() {
    var mercancia = this.construirMercancia(10, NO_EXISTENTE);
    Optional<Mercancia> mercanciaNoExistente = Optional.ofNullable(null);

    when(mercanciaRepository.findById(10)).thenReturn(mercanciaNoExistente);

    assertThrows(NoExisteException.class, () -> mercanciaService.borrar(10,1));

    verify(mercanciaRepository, times(0)).deleteById(10);
    verify(mercanciaRepository, times(1)).findById(10);
  }

  @Test
  void cuando_borrar_mercancia_y_usuario_no_propietario() {
    var mercancia = this.construirMercancia(10, YA_EXISTENTE);
    Optional<Mercancia> mercanciaNoExistente = Optional.ofNullable(null);

    when(mercanciaRepository.findById(10)).thenReturn(mercanciaNoExistente);

    assertThrows(NoExisteException.class, () -> mercanciaService.borrar(10,3));

    verify(mercanciaRepository, times(0)).deleteById(10);
    verify(mercanciaRepository, times(1)).findById(10);
  }

  @ParameterizedTest(name = "Buscar mercancia por id {0}")
  @ValueSource(ints = {5, 8, 9, 10, 50, 500, 1000})
  @DisplayName("Buscar por id y mercancia no exite arrojar mensaje")
  void test_buscar_por_id_y_marca_no_existe(int id) {
    Optional<Mercancia> mercanciaPorIdNoExiste = Optional.empty();
    when(mercanciaRepository.findById(id)).thenReturn(mercanciaPorIdNoExiste);

    assertThrows(NoExisteException.class, () -> mercanciaService.buscarPorId(id));

    verify(mercanciaRepository, times(1)).findById(id);
  }

  private Mercancia construirMercancia(int id, String nombre) {
    var mercancia = new Mercancia();
    mercancia.setId(id);
    mercancia.setNombre(nombre);
    mercancia.setCantidad(CANTIDAD);
    mercancia.setFechaRegistro(FECHA_HORA_ACTUAL);
    mercancia.setUsuario(this.crearUsuario());

    return mercancia;
  }

  private Usuario crearUsuario(){
    var usuario = new Usuario();
    usuario.setId(1);
    usuario.setNombre("Usuario de prueba");
    usuario.setEdad(19);
    usuario.setFechaIngreso(FECHA_HORA_ACTUAL);
    return usuario;
  }

  private void validarMercanciaExistente(String nombre) {
    var mercanciaExistenteConMismoNombre = this.construirMercancia(1,nombre);
    when(mercanciaRepository.findByNombre(YA_EXISTENTE))
        .thenReturn(Optional.of(mercanciaExistenteConMismoNombre));
  }
}