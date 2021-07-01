package com.prueba.BackendNexos.Service.Impl;


import com.prueba.BackendNexos.Models.Usuario;
import com.prueba.BackendNexos.Repository.UsuarioRepository;
import com.prueba.BackendNexos.Service.UsuarioService;
import com.prueba.BackendNexos.exceptions.ExisteException;
import com.prueba.BackendNexos.exceptions.NoExisteException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

  private UsuarioRepository usuarioRepository;
  private Validator validator;

  @Autowired
  public UsuarioServiceImpl(UsuarioRepository usuarioRepository, Validator validator) {
    this.usuarioRepository = usuarioRepository;
    this.validator = validator;
  }

  @Override
  public Usuario crear(@Valid Usuario usuario) {
    this.validar(usuario);
    this.usuarioRepository.save(usuario);
    return usuario;
  }

  @Override
  public Usuario actualizar(Usuario usuario) {
    this.validar(usuario);
    this.buscarPorId(usuario.getId());
    this.usuarioRepository.save(usuario);
    return usuario;
  }

  @Override
  public Usuario buscarPorId(int id) {
    return this.usuarioRepository.findById(id)
        .orElseThrow(() -> new NoExisteException("No existe Usuario con el ID: " + id));
  }

  @Override
  public List<Usuario> listar() {return this.usuarioRepository.findAll(Sort.by("id"));}

  @Override
  public void borrar(int id) {
    this.buscarPorId(id);
    this.usuarioRepository.deleteById(id);
  }

  private void validar(Usuario usuario) {
    Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
    if (!violations.isEmpty()) {
      var sb = new StringBuilder();
      for (ConstraintViolation<Usuario> constraintViolation : violations) {
        sb.append(constraintViolation.getMessage());
      }
      throw new ConstraintViolationException("Error occurred: " + sb, violations);
    }
    this.validarExistePorNombre(usuario);
  }

  private void validarExistePorNombre(Usuario usuario) {
    Optional<Usuario> usarioExistente = this.usuarioRepository.findByNombre(usuario.getNombre());
    if (usarioExistente.isPresent() && !usarioExistente.get().esElMismoId(usuario)) {
      throw new ExisteException("Ya existe el usuario con nombre " + usuario.getNombre());
    }
  }
}
