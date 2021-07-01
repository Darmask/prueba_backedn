package com.prueba.BackendNexos.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class IngresoDto {

  protected Integer id;

  @NotNull(message = "La cantidad para el  ingreso es requerida")
  private Integer cantidad;

  @NotNull(message = "Fecha de registro no puede ser vacío")
  @PastOrPresent(message = "Fecha de registro debe ser anterior o igual a la fecha actual")
  @JsonFormat(pattern = "yyyy/MM/dd")
  private LocalDate fechaIngreso;

  @NotNull(message = "El ingreso debe contener una mercancia")
  private MercanciaDto mercancia;

}
