package com.prueba.BackendNexos.Service.Impl;

import com.prueba.BackendNexos.Models.Ingreso;
import com.prueba.BackendNexos.Repository.IngresoRepository;
import com.prueba.BackendNexos.Service.IngresoService;
import com.prueba.BackendNexos.Service.MercanciaService;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class IngresoServiceImpl implements IngresoService {

  private MercanciaService mercanciaService;
  private IngresoRepository ingresoRepository;

  @Autowired
  public IngresoServiceImpl(MercanciaService mercanciaService,
      IngresoRepository ingresoRepository) {
    this.mercanciaService = mercanciaService;
    this.ingresoRepository = ingresoRepository;
  }

  @Override
  public Ingreso crear(Ingreso ingreso) {
    ingresoRepository.save(ingreso);
    this.agregarCantidad(ingreso);
    return ingreso;
  }

  @Override
  public List<Ingreso> listar() {return this.ingresoRepository.findAll(Sort.by("id"));}

  private void agregarCantidad(Ingreso ingreso) {
    var nuevaCantidad = mercanciaService.buscarPorId(ingreso.getMercancia().getId());
    nuevaCantidad.setCantidad(nuevaCantidad.getCantidad() + ingreso.getCantidad());
    mercanciaService.actualizar(nuevaCantidad);
  }
}
