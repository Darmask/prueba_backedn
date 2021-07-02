package com.prueba.BackendNexos.Service;

import com.prueba.BackendNexos.Models.Salida;
import java.util.List;

public interface SalidaService {

  Salida crear(Salida salida);

  List<Salida> listar();
}

