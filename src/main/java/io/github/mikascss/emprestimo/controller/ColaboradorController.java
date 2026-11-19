package io.github.mikascss.emprestimo.controller;

import io.github.mikascss.emprestimo.model.Colaborador;
import io.github.mikascss.emprestimo.model.Emprestimo;
import io.github.mikascss.emprestimo.model.ResultModel;
import io.github.mikascss.emprestimo.service.ColaboradorService;
import io.github.mikascss.emprestimo.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    private final ColaboradorService service;
    private final EmprestimoService emprestimoService;

    public ColaboradorController(
            ColaboradorService service,
            EmprestimoService emprestimoService) {
        this.service = service;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public ResponseEntity<ResultModel<List<Colaborador>>> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultModel<Colaborador>> buscar(
            @PathVariable Long id
    ) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<ResultModel<Colaborador>> cadastrar(
            @Valid @RequestBody Colaborador colaborador) {
        return service.cadastrar(colaborador);
    }

    @GetMapping("/{id}/emprestimos")
    public ResponseEntity<ResultModel<List<Emprestimo>>> emprestimos(
            @PathVariable Long id) {
        return emprestimoService.listarDoColaborador(id);
    }
}

