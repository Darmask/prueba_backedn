package com.prueba.BackendNexos.Service.Impl;

import com.prueba.BackendNexos.Models.Mercancia;
import com.prueba.BackendNexos.Repository.MercanciaRepository;
import com.prueba.BackendNexos.Service.MercanciaService;
import com.prueba.BackendNexos.exceptions.ExisteException;
import com.prueba.BackendNexos.exceptions.NoExisteException;
import com.prueba.BackendNexos.exceptions.NoTienePermisoParaEliminarException;
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
public class MercanciaServiceImpl implements MercanciaService {

  private MercanciaRepository mercanciaRepository;
  private Validator validator;

  @Autowired
  public MercanciaServiceImpl(
      MercanciaRepository mercanciaRepository, Validator validator) {
    this.mercanciaRepository = mercanciaRepository;
    this.validator = validator;
  }

  @Override
  public Mercancia crear(Mercancia mercancia) {
    this.validar(mercancia);
    this.mercanciaRepository.save(mercancia);
    return mercancia;
  }

  @Override
  public Mercancia actualizar(Mercancia mercancia) {
    this.validar(mercancia);
    this.buscarPorId(mercancia.getId());
    this.mercanciaRepository.save(mercancia);
    return mercancia;
  }

  @Override
  public Mercancia buscarPorId(int id) {
    return this.mercanciaRepository.findById(id)
        .orElseThrow(() -> new NoExisteException("No existe Mercancia con el ID: " + id));
  }

  @Override
  public Mercancia buscarPorNombre(String nombre) {
    return this.mercanciaRepository.findByNombre(nombre)
        .orElseThrow(
            () -> new NoExisteException("No existe mercancia con el nombre: " + nombre));
  }

  @Override
  public List<Mercancia> listar() {return this.mercanciaRepository.findAll(Sort.by("id"));}

  @Override
  public void borrar(int id, int idUsuario) {
    var mercancia = this.buscarPorId(id);
    this.BuscarPropietario(mercancia,idUsuario);
    this.mercanciaRepository.deleteById(id);
  }

  private void validar(Mercancia mercancia) {
    Set<ConstraintViolation<Mercancia>> violations = validator.validate(mercancia);
    if (!violations.isEmpty()) {
      var sb = new StringBuilder();
      for (ConstraintViolation<Mercancia> constraintViolation : violations) {
        sb.append(constraintViolation.getMessage());
      }
      throw new ConstraintViolationException("Error: " + sb, violations);
    }
    this.validarExistePorNombre(mercancia);
  }

  private void validarExistePorNombre(Mercancia mercancia) {
    Optional<Mercancia> mercanciaExistente = this.mercanciaRepository
        .findByNombre(mercancia.getNombre());
    if (mercanciaExistente.isPresent() && !mercanciaExistente.get().esElMismoId(mercancia)) {
      throw new ExisteException("Ya existe la mercancia  con nombre " + mercancia.getNombre());
    }
  }

  private void BuscarPropietario(Mercancia mercancia, int idUsuario) {
    if (mercancia.getUsuario().getId() != idUsuario){
      throw new NoTienePermisoParaEliminarException(
          "Usted no tiene permisos para eliminar esta mercancia: " + mercancia.getNombre());
    }
  }
}
