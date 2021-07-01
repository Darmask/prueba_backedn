package com.prueba.BackendNexos.Service.Impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prueba.BackendNexos.Models.Cargo;
import com.prueba.BackendNexos.Repository.CargoRepository;
import com.prueba.BackendNexos.exceptions.ExisteException;
import com.prueba.BackendNexos.exceptions.NoExisteException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class CargoServiceImplTest {
  @Mock
  private static CargoRepository cargoRepository;

  @Mock
  private static LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

  @InjectMocks
  private static CargoServiceImpl cargoService;

  private final static String NOMBRE_NO_EXISTENTE = "a";
  private final static String PRINCIPAL = "Principal";
  private final static String SUPLENTE = "Suplente";



  @Test
  void cuando_crea_y_nombre_no_existe_deberia_crear_cargo() {
    when(cargoRepository.findByNombre(NOMBRE_NO_EXISTENTE)).thenReturn(Optional.ofNullable(null));
    Cargo cargo = this.construirCargo(0, NOMBRE_NO_EXISTENTE);

    cargoService.crear(cargo);

    verify(cargoRepository, times(1))
        .findByNombre(cargo.getNombre());
    verify(cargoRepository, times(1))
        .save(cargo);
  }

  @Test
  void cuando_crea_y_nombre_existe_deberia_lanzar_exception() {
    Cargo cargo = this.construirCargo(0, PRINCIPAL);
    this.configurarCargoExistente(PRINCIPAL);

    assertThrows(ExisteException.class, () -> cargoService.crear(cargo));

    verify(cargoRepository, times(1))
        .findByNombre(cargo.getNombre());
    verify(cargoRepository, times(0))
        .save(cargo);
  }

  @Test
  void cuando_actualiza_y_cargo_no_existe_deberia_lanzar_exception() {
    Cargo cargo = this.construirCargo(10, PRINCIPAL);
    when(cargoRepository.findById(cargo.getId())).thenReturn(Optional.ofNullable(null));

    assertThrows(RuntimeException.class, () -> cargoService.actualizar(cargo));

    verify(cargoRepository, times(1))
        .findById(cargo.getId());
    verify(cargoRepository, times(0))
        .save(cargo);
  }

  @Test
  void cuando_actualiza_y_nombre_existe_deberia_lanzar_exception() {
    Cargo cargo = this.construirCargo(10, PRINCIPAL);
    this.configurarCargoExistente(PRINCIPAL);
    when(cargoRepository.findById(cargo.getId())).thenReturn(Optional.of(cargo));

    assertThrows(ExisteException.class, () -> cargoService.actualizar(cargo));

    verify(cargoRepository, times(1))
        .findById(cargo.getId());
    verify(cargoRepository, times(1))
        .findByNombre(PRINCIPAL);
    verify(cargoRepository, times(0))
        .save(cargo);
  }

  @Test
  void cuando_actualiza_con_mismo_nombre() {
    Cargo cargo = this.construirCargo(10, PRINCIPAL);
    when(cargoRepository.findById(cargo.getId())).thenReturn(Optional.of(cargo));
    when(cargoRepository.findByNombre(PRINCIPAL)).thenReturn(Optional.of(cargo));

    cargoService.actualizar(cargo);

    verify(cargoRepository, times(1))
        .findById(cargo.getId());
    verify(cargoRepository, times(1))
        .findByNombre(PRINCIPAL);
    verify(cargoRepository, times(1))
        .save(cargo);
  }

  @Test
  void cuando_existe_cargo_buscar_por_id() {
    Cargo cargo = this.construirCargo(10, PRINCIPAL);

    when(cargoRepository.findById(cargo.getId()))
        .thenReturn(Optional.of(cargo));

    Cargo cargoEncontrado = cargoService.buscarPorId(cargo.getId());
    assertThat(cargoEncontrado).isNotNull();
    assertThat(cargoEncontrado.getId()).isEqualTo(10);
  }

  @Test
  void cuando_no_existe_cargo_buscar_por_id() {
    Optional<Cargo> cargoNoExistente = Optional.ofNullable(null);
    when(cargoRepository.findById(5)).thenReturn(cargoNoExistente);

    assertThrows(NoExisteException.class, () -> cargoService.buscarPorId(5));

    verify(cargoRepository, times(1)).findById(5);
  }

  @Test
  void listar() {
    Sort sort = Sort.by("id");
    when(cargoRepository.findAll(sort)).thenReturn(this.obtenerListaCargos());

    List<Cargo> cargos = cargoService.listar();

    assertThat(cargos).isNotNull().isNotEmpty();
    verify(cargoRepository, times(1)).findAll(sort);
  }

  @Test
  void cuando_borrar_cargo_existe() {
    Cargo cargo = this.construirCargo(10, PRINCIPAL);
    Optional<Cargo> cargoExistente = Optional.of(cargo);

    when(cargoRepository.findById(10)).thenReturn(cargoExistente);

    cargoService.borrar(10);
    verify(cargoRepository, times(1)).deleteById(10);
  }

  @Test
  void cuando_borrar_cargo_no_existe() {
    Cargo cargo = this.construirCargo(10, PRINCIPAL);
    Optional<Cargo> cargoExistente = Optional.ofNullable(null);

    when(cargoRepository.findById(10)).thenReturn(cargoExistente);

    assertThrows(NoExisteException.class, () -> cargoService.borrar(10));
    verify(cargoRepository, times(0)).deleteById(10);
    verify(cargoRepository, times(1)).findById(10);
  }

  private Cargo construirCargo(int id, String nombre) {
    Cargo cargo = new Cargo();
    cargo.setId(id);
    cargo.setNombre(nombre);
    return cargo;
  }

  private List<Cargo> obtenerListaCargos() {
    Cargo cargo1 = this.construirCargo(1, PRINCIPAL);
    Cargo cargo2 = this.construirCargo(2, SUPLENTE);
    return Arrays.asList(cargo1, cargo2);
  }

  private void configurarCargoExistente(String nombre) {
    Cargo cargoExistenteConMismoNombre = new Cargo();
    cargoExistenteConMismoNombre.setId(100);
    cargoExistenteConMismoNombre.setNombre(nombre);
    when(cargoRepository.findByNombre(nombre))
        .thenReturn(Optional.of(cargoExistenteConMismoNombre));
  }
}