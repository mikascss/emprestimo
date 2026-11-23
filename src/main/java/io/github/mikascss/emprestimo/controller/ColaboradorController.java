package io.github.mikascss.emprestimo.controller;

import io.github.mikascss.emprestimo.exception.ErroResposta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Colaboradores", description = "Cadastro e consulta de colaboradores.")
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

    @Operation(summary = "Listar colaboradores")
    @ApiResponse(responseCode = "200", description = "Lista de colaboradores", useReturnTypeSchema = true)
    @GetMapping
    public ResponseEntity<ResultModel<List<Colaborador>>> listar() {
        return service.listar();
    }

    @Operation(summary = "Buscar colaborador por ID")
    @ApiResponse(responseCode = "200", description = "Colaborador encontrado", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "ID inválido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Colaborador não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @GetMapping("/{id}")
    public ResponseEntity<ResultModel<Colaborador>> buscar(
            @PathVariable Long id
    ) {
        return service.buscar(id);
    }

    @Operation(summary = "Cadastrar colaborador")
    @ApiResponse(responseCode = "201", description = "Colaborador cadastrado", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @PostMapping
    public ResponseEntity<ResultModel<Colaborador>> cadastrar(
            @Valid @RequestBody Colaborador colaborador) {
        return service.cadastrar(colaborador);
    }

    @Operation(summary = "Listar empréstimos do colaborador")
    @ApiResponse(responseCode = "200", description = "Lista de empréstimos; vazia se não houver registros para o ID", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "ID inválido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @GetMapping("/{id}/emprestimos")
    public ResponseEntity<ResultModel<List<Emprestimo>>> emprestimos(
            @PathVariable Long id) {
        return emprestimoService.listarDoColaborador(id);
    }
}

