package com.prueba.BackendNexos.Models;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class Auditoria {

  public abstract int getId();

  @Column(name = "modificado_en")
  @LastModifiedDate
  private LocalDateTime modificadoEn;

  @Column(name = "modificado_por")
  private String modificadoPor;

  public boolean esElMismoId(Auditoria o) {
    return (o == this || (o.getId() == this.getId()));
  }
}
