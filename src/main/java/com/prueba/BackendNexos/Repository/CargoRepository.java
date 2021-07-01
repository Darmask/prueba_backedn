package com.prueba.BackendNexos.Repository;

import com.prueba.BackendNexos.Models.Cargo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {
  Optional<Cargo> findByNombre(String nombre);

}
