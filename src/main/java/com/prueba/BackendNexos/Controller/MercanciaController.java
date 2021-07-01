package com.prueba.BackendNexos.Controller;

import com.prueba.BackendNexos.Dto.MercanciaDto;
import com.prueba.BackendNexos.Models.Mercancia;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.prueba.BackendNexos.exceptions.NoExisteException;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

import com.prueba.BackendNexos.Service.MercanciaService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/nexos/mercancia", produces = MediaType.APPLICATION_JSON_VALUE)
public class MercanciaController {

  private MercanciaService mercanciaService;
  private ModelMapper modelMapper;

  @Autowired
  public MercanciaController(
      MercanciaService mercanciaService, ModelMapper modelMapper) {
    this.mercanciaService = mercanciaService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public ResponseEntity<List<MercanciaDto>> listar() {
    List<MercanciaDto> mercanciaDtos = this.mercanciaService.listar()
        .stream()
        .map(mercancia -> modelMapper.map(mercancia, MercanciaDto.class))
        .collect(Collectors.toList());
    return new ResponseEntity<>(mercanciaDtos, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<MercanciaDto> crear(@Valid @RequestBody MercanciaDto mercanciaDto) {
    var mercancia = this.modelMapper.map(mercanciaDto, Mercancia.class);
    mercancia = this.mercanciaService.crear(mercancia);
    return new ResponseEntity<>(modelMapper.map(mercancia, MercanciaDto.class), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MercanciaDto> actualizar(@Valid @RequestBody MercanciaDto mercanciaDto,
      @PathVariable("id") int id) {
    if (mercanciaDto == null || mercanciaDto.getId() != id) {
      throw new NoExisteException("El id de la mercancia  a actualizar no corresponde al parametro recibido");
    }

    var mercancia = this.modelMapper.map(mercanciaDto, Mercancia.class);
    mercancia = this.mercanciaService.actualizar(mercancia);
    return new ResponseEntity<>(modelMapper.map(mercancia, MercanciaDto.class), HttpStatus.OK);
  }

  @DeleteMapping("/{id}/{idUsuario}")
  public ResponseEntity eliminar(@PathVariable("id") int id,@PathVariable("idUsuario") int idUsuario) {
    mercanciaService.borrar(id,idUsuario);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
