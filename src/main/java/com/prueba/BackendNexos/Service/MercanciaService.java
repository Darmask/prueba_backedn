package com.prueba.BackendNexos.Service;

import com.prueba.BackendNexos.Models.Mercancia;
import java.util.List;
import org.springframework.data.domain.Page;

public interface MercanciaService {
  Mercancia crear(Mercancia mercancia);

  Mercancia actualizar(Mercancia mercancia);

  Mercancia buscarPorId(int id);
  Mercancia buscarPorNombre(String nombre);

  List<Mercancia>listar();

  void borrar(int id, int idUsuario);
}
