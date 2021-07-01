package com.prueba.BackendNexos.Dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CargoDto {
  protected Integer id;

  @NotNull(message = "El nombre del cargo  no puede ser vacío")
  private String nombre;
}
