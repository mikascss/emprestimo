package io.github.mikascss.emprestimo.controller;

import io.github.mikascss.emprestimo.dto.equipamento.EquipamentoRegisterRequest;
import io.github.mikascss.emprestimo.dto.equipamento.EquipamentoUpdateRequest;
import io.github.mikascss.emprestimo.model.Equipamento;
import io.github.mikascss.emprestimo.model.ResultModel;
import io.github.mikascss.emprestimo.model.TipoEquipamento;
import io.github.mikascss.emprestimo.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService){
        this.equipamentoService = equipamentoService;
    }

    @GetMapping
    public ResponseEntity<ResultModel<List<Equipamento>>> listar(
            @RequestParam(required = false) @Valid TipoEquipamento tipo,
            @RequestParam(required = false) @Valid Boolean disponivel
            ){
        return equipamentoService.listar(tipo, disponivel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultModel<Equipamento>> buscar(
            @PathVariable Long id
    ){
        return equipamentoService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<ResultModel<Equipamento>> cadastrar(
            @RequestBody @Valid EquipamentoRegisterRequest equipamento
    ){
        return equipamentoService.cadastrar(equipamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultModel<Equipamento>> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EquipamentoUpdateRequest dados
    ){
        return equipamentoService.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultModel<Void>> remover(
            @PathVariable Long id
    ){
        return equipamentoService.remover(id);
    }
}
