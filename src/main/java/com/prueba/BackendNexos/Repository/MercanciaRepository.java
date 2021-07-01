package com.prueba.BackendNexos.Repository;

import com.prueba.BackendNexos.Models.Mercancia;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercanciaRepository extends JpaRepository<Mercancia, Integer> {

  Optional<Mercancia> findByNombre(String nombre);
}
