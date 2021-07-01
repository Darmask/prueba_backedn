package com.prueba.BackendNexos.Service;

import com.prueba.BackendNexos.Models.Cargo;
import java.util.List;

public interface CargoService {

  Cargo crear(Cargo cargo);

  Cargo actualizar(Cargo cargo);

  Cargo buscarPorId(int id);

  List<Cargo> listar();

  void borrar(int id);

}
