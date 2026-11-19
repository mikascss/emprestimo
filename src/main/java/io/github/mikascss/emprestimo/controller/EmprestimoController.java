package io.github.mikascss.emprestimo.controller;

import io.github.mikascss.emprestimo.dto.EmprestimoRequest;
import io.github.mikascss.emprestimo.model.Emprestimo;
import io.github.mikascss.emprestimo.model.ResultModel;
import io.github.mikascss.emprestimo.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/emprestimo")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService){
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<ResultModel<Emprestimo>> emprestar(
            @RequestBody @Valid EmprestimoRequest pedido) {
        return emprestimoService.emprestar(pedido);
    }
    @PutMapping("/{id}/devolucao")
    public ResponseEntity<ResultModel<Emprestimo>> devolver(
            @PathVariable Long id) {
        return emprestimoService.devolver(id);
    }

    @GetMapping
    public ResponseEntity<ResultModel<List<Emprestimo>>> listar(
            @RequestParam(required = false)
            Boolean atrasados) {
        return emprestimoService.listar(atrasados);
    }
}
