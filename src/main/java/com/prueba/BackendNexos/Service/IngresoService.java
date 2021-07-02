package com.prueba.BackendNexos.Service;

import com.prueba.BackendNexos.Models.Ingreso;
import java.util.List;

public interface IngresoService {

  Ingreso crear(Ingreso ingreso);

  List<Ingreso>listar();
}
