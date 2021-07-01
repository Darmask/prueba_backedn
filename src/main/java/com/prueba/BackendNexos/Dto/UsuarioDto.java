package com.prueba.BackendNexos.Dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UsuarioDto {

  protected Integer id;

  @NotNull(message = "El nombre no puede ser vacío")
  private String nombre;

  @NotNull(message = "La edad  no puede ser vacío")
  private Integer edad;

  @NotNull(message = "Fecha de registro no puede ser null")
  @JsonFormat(pattern = "yyyy/MM/dd")
  private LocalDate fechaIngreso;

  @NotNull(message = "El cargo del usuario es requerido")
  private CargoDto cargo;
}
