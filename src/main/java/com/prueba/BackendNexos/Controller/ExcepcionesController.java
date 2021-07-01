package com.prueba.BackendNexos.Controller;

import com.prueba.BackendNexos.Dto.MensajeError;
import com.prueba.BackendNexos.exceptions.ExisteException;
import com.prueba.BackendNexos.exceptions.NoExisteException;
import com.prueba.BackendNexos.exceptions.NoTienePermisoParaEliminarException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExcepcionesController extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ExisteException.class)
  public ResponseEntity<MensajeError> objetoExistente(ExisteException ex) {
    var respuesta = new MensajeError(ex.getMessage());
    return ResponseEntity.unprocessableEntity().body(respuesta);
  }

  @ExceptionHandler(NoExisteException.class)
  public ResponseEntity<MensajeError> objetoNoExistente(NoExisteException ex) {
    var respuesta = new MensajeError(ex.getMessage());
    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NoTienePermisoParaEliminarException.class)
  public ResponseEntity<MensajeError> sinPermisosParaEliminar(NoTienePermisoParaEliminarException ex) {
    var respuesta = new MensajeError(ex.getMessage());
    return new ResponseEntity<>(respuesta, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<MensajeError> errorDelServidor(Exception ex) {
    ex.printStackTrace();
    var respuesta = new MensajeError("Ocurrio un error inesperado");
    return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({ConstraintViolationException.class,RuntimeException.class})
  public ResponseEntity<MensajeError> datosIncorrectos(Exception ex) {
    ex.printStackTrace();
    var respuesta = new MensajeError(ex.getMessage());
    return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException exception, @NonNull HttpHeaders headers,
      @NonNull HttpStatus status,
      WebRequest request) {
    List<String> validationErrors = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.toList());
    return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, validationErrors);
  }
  private ResponseEntity<Object> getExceptionResponseEntity(final HttpStatus status,
      List<String> errors) {
    List<MensajeError> errores =
        !CollectionUtils.isEmpty(errors) ? errors.stream().map(MensajeError::new)
            .collect(Collectors.toList())
            : Collections.singletonList(new MensajeError(status.getReasonPhrase()));
    return new ResponseEntity<>(errores, status);
  }


}
