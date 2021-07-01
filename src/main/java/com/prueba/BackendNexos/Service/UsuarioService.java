package com.prueba.BackendNexos.Service;

import com.prueba.BackendNexos.Models.Usuario;
import java.util.List;

public interface UsuarioService {

  Usuario crear(Usuario usuario);

  Usuario actualizar(Usuario usuario);

  Usuario buscarPorId(int id);

  List<Usuario> listar();

  void borrar(int id);
}
