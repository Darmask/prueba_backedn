package com.prueba.BackendNexos.Dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MercanciaDto {
  protected Integer id;

  private String nombre;

  @NotNull(message = "La cantidad para registrar la mercancia es requerido")
  private Integer cantidad;


  @NotNull(message = "Fecha de registro no puede ser vacío")
  @PastOrPresent(message = "Fecha de registro debe ser anterior o igual a la fecha actual")
  @JsonFormat(pattern = "yyyy/MM/dd")
  private LocalDate fechaRegistro;

  @NotNull(message = "Se debe asignar un usuario a la mercancia")
  private UsuarioDto usuario;
}
