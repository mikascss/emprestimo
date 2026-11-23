package io.github.mikascss.emprestimo.controller;

import io.github.mikascss.emprestimo.exception.ErroResposta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Empréstimos", description = "Registro de empréstimos, devoluções e consulta de atrasos.")
@RestController
@RequestMapping("/emprestimo")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService){
        this.emprestimoService = emprestimoService;
    }

    @Operation(summary = "Registrar empréstimo de equipamento")
    @ApiResponse(responseCode = "201", description = "Empréstimo criado com prazo calculado pelo tipo do equipamento", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Equipamento ou colaborador não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "409", description = "Equipamento indisponível ou colaborador com empréstimo atrasado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @PostMapping
    public ResponseEntity<ResultModel<Emprestimo>> emprestar(
            @RequestBody @Valid EmprestimoRequest pedido) {
        return emprestimoService.emprestar(pedido);
    }
    @Operation(summary = "Registrar devolução e liberar equipamento")
    @ApiResponse(responseCode = "200", description = "Devolução registrada", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "ID inválido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "409", description = "Empréstimo já devolvido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @PutMapping("/{id}/devolucao")
    public ResponseEntity<ResultModel<Emprestimo>> devolver(
            @PathVariable Long id) {
        return emprestimoService.devolver(id);
    }

    @Operation(summary = "Listar empréstimos")
    @ApiResponse(responseCode = "200", description = "Lista de empréstimos", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Filtro inválido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @GetMapping
    public ResponseEntity<ResultModel<List<Emprestimo>>> listar(
            @Parameter(description = "true: somente atrasados ainda não devolvidos; false ou omitido: todos")
            @RequestParam(required = false)
            Boolean atrasados) {
        return emprestimoService.listar(atrasados);
    }
}
