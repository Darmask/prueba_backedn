package com.prueba.BackendNexos.Repository;

import com.prueba.BackendNexos.Models.Salida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalidaRepository extends JpaRepository<Salida, Integer> {

}
