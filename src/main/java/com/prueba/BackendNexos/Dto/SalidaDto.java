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
public class SalidaDto {

  protected Integer id;

  @NotNull(message = "La cantidad para la salida es requerida")
  private Integer cantidad;

  @NotNull(message = "Fecha de la salida no puede ser vacío")
  @JsonFormat(pattern = "yyyy/MM/dd")
  private LocalDate fechaIngreso;

  @NotNull(message = "La salida debe tener una mercancia asignada")
  private MercanciaDto mercancia;

}
