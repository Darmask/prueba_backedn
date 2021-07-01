package com.prueba.BackendNexos.Controller;

import com.prueba.BackendNexos.Dto.SalidaDto;
import com.prueba.BackendNexos.Models.Salida;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.prueba.BackendNexos.Service.SalidaService;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/nexos/salida", produces = MediaType.APPLICATION_JSON_VALUE)
public class SalidaController {

  private SalidaService salidaService;
  private ModelMapper modelMapper;

  @Autowired
  public SalidaController(SalidaService salidaService, ModelMapper modelMapper) {
    this.salidaService = salidaService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public ResponseEntity<List<SalidaDto>> listar() {
    List<SalidaDto> salidaDtos = this.salidaService.listar()
        .stream()
        .map(salida -> modelMapper.map(salida, SalidaDto.class))
        .collect(Collectors.toList());
    return new ResponseEntity<>(salidaDtos, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<SalidaDto> crear(@Valid @RequestBody SalidaDto salidaDto) {
    var salida = this.modelMapper.map(salidaDto, Salida.class);
    salida = this.salidaService.crear(salida);
    return new ResponseEntity<>(modelMapper.map(salida, SalidaDto.class), HttpStatus.CREATED);
  }
}
