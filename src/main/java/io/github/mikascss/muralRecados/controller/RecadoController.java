package io.github.mikascss.muralRecados.controller;

import io.github.mikascss.muralRecados.model.Recado;
import io.github.mikascss.muralRecados.service.RecadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recados")
public class RecadoController {

    private final RecadoService recadoService;

    public RecadoController(RecadoService recadoService){
        this.recadoService = recadoService;
    }

    @GetMapping
    public List<Recado> listar(){
        return recadoService.listar();
    }

    @PostMapping
    public ResponseEntity<Recado> cadastrar(
            @RequestBody Recado recado
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recadoService.cadastrar(recado));
    }
}
