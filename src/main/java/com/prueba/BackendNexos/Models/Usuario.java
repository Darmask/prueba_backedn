package com.prueba.BackendNexos.Models;

import java.time.LocalDate;
import javax.persistence.GenerationType;
import javax.persistence.FetchType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario extends Auditoria{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private int id;

  @Column(name = "user_nombres")
  @NotBlank(message = "El nombre del usuario no puede ser vacío")
  @Size(min = 1, max = 255, message = "El nombre debe tener mínimo un carácter y máximo 255")
  private String nombre;

  @Column(name = "user_edad")
  @NotNull(message = "La edad del usuario no puede ser vacío")
  private Integer edad;

  @Column(name = "user_fecha_ingreso")
  @NotNull(message = "Fecha de registro del usuario no puede ser vacío")
  private LocalDate fechaIngreso;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_car_id")
  @NotNull(message = "El usuario debe contener un cargo")
  private Cargo cargo;

}
