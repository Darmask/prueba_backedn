package com.prueba.BackendNexos.exceptions;

public class NoTienePermisoParaEliminarException extends RuntimeException{
  public NoTienePermisoParaEliminarException(String message) {
    super(message);
  }
}
