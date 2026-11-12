package io.github.mikascss.muralRecados.service.impl;

import io.github.mikascss.muralRecados.model.Recado;
import io.github.mikascss.muralRecados.repository.RecadoRepository;
import io.github.mikascss.muralRecados.service.RecadoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecadoServiceImpl implements RecadoService {

    private final RecadoRepository recadoRepository;

    public RecadoServiceImpl(RecadoRepository recadoRepository){
        this.recadoRepository = recadoRepository;
    }

    @Override
    public List<Recado> listar(){
        return recadoRepository.findAll();
    }

    @Override
    public Recado cadastrar(Recado recado){
        return recadoRepository.save(recado);
    }
}
