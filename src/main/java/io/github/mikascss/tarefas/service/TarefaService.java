package io.github.mikascss.tarefas.service;

import io.github.mikascss.tarefas.model.Tarefa;
import io.github.mikascss.tarefas.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository){
        this.tarefaRepository = tarefaRepository;
    }

    public List<Tarefa> listar(){
        return tarefaRepository.findAll();
    }

    public Tarefa cadastrar(Tarefa tarefa){
        return tarefaRepository.save(tarefa);
    }
}
