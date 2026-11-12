package io.github.mikascss.muralRecados.service;

import io.github.mikascss.muralRecados.model.Recado;

import java.util.List;

public interface RecadoService {
    List<Recado> listar();

    Recado cadastrar(Recado recado);
}
