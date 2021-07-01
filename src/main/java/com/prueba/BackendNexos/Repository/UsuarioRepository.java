package com.prueba.BackendNexos.Repository;

import com.prueba.BackendNexos.Models.Usuario;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  Page<Usuario> findAll(Pageable pageable);

  Optional<Usuario> findByNombre(String nombre);
}
