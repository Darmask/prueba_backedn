package com.prueba.BackendNexos.Controller;

import com.prueba.BackendNexos.Dto.CargoDto;
import com.prueba.BackendNexos.Dto.IngresoDto;
import com.prueba.BackendNexos.Models.Ingreso;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;

import com.prueba.BackendNexos.Service.IngresoService;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/nexos/ingreso", produces = MediaType.APPLICATION_JSON_VALUE)
public class IngresoController {

  private IngresoService ingresoService;
  private ModelMapper modelMapper;

  @Autowired
  public IngresoController(IngresoService ingresoService, ModelMapper modelMapper) {
    this.ingresoService = ingresoService;
    this.modelMapper = modelMapper;
  }
  @GetMapping
  public ResponseEntity<List<IngresoDto>> listar() {
    List<IngresoDto> ingresoDtos = this.ingresoService.listar()
        .stream()
        .map(ingreso -> modelMapper.map(ingreso, IngresoDto.class))
        .collect(Collectors.toList());
    return new ResponseEntity<>(ingresoDtos, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<IngresoDto> crear(@Valid @RequestBody IngresoDto ingresoDto) {
    var ingreso = this.modelMapper.map(ingresoDto, Ingreso.class);
    ingreso = this.ingresoService.crear(ingreso);
    return new ResponseEntity<>(modelMapper.map(ingreso, IngresoDto.class), HttpStatus.CREATED);
  }

}
