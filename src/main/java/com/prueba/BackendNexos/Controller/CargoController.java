package com.prueba.BackendNexos.Controller;

import com.prueba.BackendNexos.Dto.CargoDto;
import com.prueba.BackendNexos.Models.Cargo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.modelmapper.ModelMapper;
import java.util.stream.Collectors;

import com.prueba.BackendNexos.Service.CargoService;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/nexos/cargos", produces = MediaType.APPLICATION_JSON_VALUE)
public class CargoController {

  private CargoService cargoService;
  private ModelMapper modelMapper;

  @Autowired
  public CargoController(CargoService cargoService, ModelMapper modelMapper) {
    this.cargoService = cargoService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public ResponseEntity<List<CargoDto>> listar() {
    List<CargoDto> cargos = this.cargoService.listar()
        .stream()
        .map(cargo -> modelMapper.map(cargo, CargoDto.class))
        .collect(Collectors.toList());
    return new ResponseEntity<>(cargos, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<CargoDto> crear(@Valid @RequestBody CargoDto cargoDto) {
    var cargo = this.modelMapper.map(cargoDto, Cargo.class);
    cargo = this.cargoService.crear(cargo);
    return new ResponseEntity<>(modelMapper.map(cargo, CargoDto.class), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CargoDto> actualizar(@Valid @RequestBody CargoDto cargoDto,
      @PathVariable("id") int id) {
    if (cargoDto == null || cargoDto.getId() != id) {
      throw new RuntimeException("El id del cargo a actualizar no corresponde al parametro recibido");
    }

    var cargo = this.modelMapper.map(cargoDto, Cargo.class);
    cargo = this.cargoService.actualizar(cargo);
    return new ResponseEntity<>(modelMapper.map(cargo, CargoDto.class), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity eliminar(@PathVariable("id") int id) {
    cargoService.borrar(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
