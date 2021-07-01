package com.prueba.BackendNexos.Service.Impl;

import com.prueba.BackendNexos.Models.Salida;
import com.prueba.BackendNexos.Repository.SalidaRepository;
import com.prueba.BackendNexos.Service.MercanciaService;
import com.prueba.BackendNexos.Service.SalidaService;
import com.prueba.BackendNexos.exceptions.ExisteException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SalidaServiceImpl implements SalidaService {

  private SalidaRepository salidaRepository;
  private MercanciaService mercanciaService;

  @Autowired
  public SalidaServiceImpl(SalidaRepository salidaRepository,
      MercanciaService mercanciaService) {
    this.salidaRepository = salidaRepository;
    this.mercanciaService = mercanciaService;
  }

  @Override
  public Salida crear(Salida salida) {
    this.restarCantidad(salida);
    salidaRepository.save(salida);
    return salida;
  }

  @Override
  public List<Salida> listar() {return this.salidaRepository.findAll(Sort.by("id"));}

  private void restarCantidad(Salida salida) {
    var nuevaCantidad = mercanciaService.buscarPorId(salida.getMercancia().getId());
    if (salida.getCantidad() > nuevaCantidad.getCantidad()){
      throw new ExisteException("No cuenta con la cantidad suficiente para realizar la salida del material ");
    }
    nuevaCantidad.setCantidad(nuevaCantidad.getCantidad() - salida.getCantidad());
    mercanciaService.actualizar(nuevaCantidad);
  }
}
