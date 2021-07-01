package com.prueba.BackendNexos.Models;

import java.time.LocalDate;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "ingresos")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ingreso extends Auditoria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ColumnDefault("default")
  @Column(name = "ing_id")
  private int id;

  @Column(name = "ing_fecha_ingreso")
  @PastOrPresent(message = "Fecha de ingreso debe ser anterior o igual a la fecha actual")
  @NotNull(message = "La fecha de ingreso no puede ser vacío")
  private LocalDate fechaIngreso;

  @Column(name = "ing_cantidad")
  @NotNull(message = "La cantidad a registrar  no puede estar  vacía")
  @Min(value = 1, message = "La cantidad debe tener mínimo un carácter")
  private Integer cantidad;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ing_mer_id")
  @NotNull(message = "Debe contener una  mercancia para realizar el  ingreso")
  private Mercancia mercancia;

}
