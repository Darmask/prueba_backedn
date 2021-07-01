package com.prueba.BackendNexos.Controller;


import com.prueba.BackendNexos.Dto.UsuarioDto;
import com.prueba.BackendNexos.Models.Usuario;
import com.prueba.BackendNexos.Service.UsuarioService;
import com.prueba.BackendNexos.exceptions.NoExisteException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/nexos/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController {

  private UsuarioService usuarioService;
  private ModelMapper modelMapper;

  @Autowired
  public UsuarioController(UsuarioService usuarioService, ModelMapper modelMapper) {
    this.usuarioService = usuarioService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public ResponseEntity<List<UsuarioDto>> listar() {
    List<UsuarioDto> usuarioDtos = this.usuarioService.listar()
        .stream()
        .map(usuario -> modelMapper.map(usuario, UsuarioDto.class))
        .collect(Collectors.toList());
    return new ResponseEntity<>(usuarioDtos, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<UsuarioDto> crear(@Valid @RequestBody UsuarioDto usuarioDto) {
    var usuario = this.modelMapper.map(usuarioDto, Usuario.class);
    usuario = this.usuarioService.crear(usuario);
    return new ResponseEntity<>(modelMapper.map(usuario, UsuarioDto.class), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UsuarioDto> actualizar(@Valid @RequestBody UsuarioDto usuarioDto,
      @PathVariable("id") int id) {
    if (usuarioDto == null || usuarioDto.getId() != id) {
      throw new NoExisteException("El id del usuario a actualizar no corresponde al parametro recibido");
    }
    var usuario = this.modelMapper.map(usuarioDto, Usuario.class);
    usuario = this.usuarioService.actualizar(usuario);
    return new ResponseEntity<>(modelMapper.map(usuario, UsuarioDto.class), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity eliminar(@PathVariable("id") int id) {
    usuarioService.borrar(id);
    return new ResponseEntity<>("Usuario eliminado", HttpStatus.OK);
  }
}
