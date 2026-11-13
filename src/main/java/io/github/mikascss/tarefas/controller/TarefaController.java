package io.github.mikascss.tarefas.controller;

import io.github.mikascss.tarefas.model.Tarefa;
import io.github.mikascss.tarefas.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService){
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listar(){
        return ResponseEntity.ok().body(tarefaService.listar());
    }

    @PostMapping
    public ResponseEntity<Tarefa> cadastrar(
            @RequestBody Tarefa tarefa
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaService.cadastrar(tarefa));
    }
}
