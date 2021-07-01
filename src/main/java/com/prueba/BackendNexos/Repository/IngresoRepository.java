package com.prueba.BackendNexos.Repository;

import com.prueba.BackendNexos.Models.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Integer> {

}
