package com.prueba.BackendNexos.Service.Impl;

import com.prueba.BackendNexos.Models.Cargo;
import com.prueba.BackendNexos.Repository.CargoRepository;
import com.prueba.BackendNexos.Service.CargoService;
import com.prueba.BackendNexos.exceptions.ExisteException;
import com.prueba.BackendNexos.exceptions.NoExisteException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CargoServiceImpl implements CargoService {

  private CargoRepository cargoRepository;
  private Validator validator;

  @Autowired
  public CargoServiceImpl(CargoRepository cargoRepository, Validator validator) {
    this.cargoRepository = cargoRepository;
    this.validator = validator;
  }

  @Override
  public Cargo crear(Cargo cargo) {
    this.validar(cargo);
    this.cargoRepository.save(cargo);
    return cargo;
  }

  @Override
  public Cargo actualizar(Cargo cargo) {
    this.buscarPorId(cargo.getId());
    this.validar(cargo);
    this.cargoRepository.save(cargo);
    return cargo;
  }

  @Override
  public Cargo buscarPorId(int id) {
    return this.cargoRepository
        .findById(id)
        .orElseThrow(() -> new NoExisteException("No existe el cargo con id " + id));
  }

  @Override
  public List<Cargo> listar() {
    return this.cargoRepository.findAll(Sort.by("id"));
  }

  @Override
  public void borrar(int id) {
    this.buscarPorId(id);
    this.cargoRepository.deleteById(id);
  }

  private void validar(Cargo cargo){
    Set<ConstraintViolation<Cargo>> violations = validator.validate(cargo);
    if (!violations.isEmpty()) {
      var sb = new StringBuilder();
      for (ConstraintViolation<Cargo> constraintViolation : violations) {
        sb.append(constraintViolation.getMessage());
      }
      throw new ConstraintViolationException("Error occurred: " + sb, violations);
    }
    this.validarExistePorNombre(cargo);
  }

  private void validarExistePorNombre(Cargo cargo) {
    Optional<Cargo> cargoExistente = this.cargoRepository.findByNombre(cargo.getNombre());
    if (cargoExistente.isPresent() && !cargoExistente.get().esElMismoId(cargo)) {
      throw new ExisteException("Ya existe el cargo con nombre " + cargo.getNombre());
    }
  }
}
