package io.github.mikascss.emprestimo.controller;

import io.github.mikascss.emprestimo.exception.ErroResposta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Equipamentos", description = "Cadastro e consulta de equipamentos.")
@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService){
        this.equipamentoService = equipamentoService;
    }

    @Operation(summary = "Listar equipamentos com filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Lista de equipamentos", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Filtro inválido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @GetMapping
    public ResponseEntity<ResultModel<List<Equipamento>>> listar(
            @Parameter(description = "Tipo do equipamento")
            @RequestParam(required = false) @Valid TipoEquipamento tipo,
            @Parameter(description = "true: disponíveis; false: indisponíveis; omitido: todos")
            @RequestParam(required = false) @Valid Boolean disponivel
            ){
        return equipamentoService.listar(tipo, disponivel);
    }

    @Operation(summary = "Buscar equipamento por ID")
    @ApiResponse(responseCode = "200", description = "Equipamento encontrado", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "ID inválido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Equipamento não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @GetMapping("/{id}")
    public ResponseEntity<ResultModel<Equipamento>> buscar(
            @PathVariable Long id
    ){
        return equipamentoService.buscar(id);
    }

    @Operation(summary = "Cadastrar equipamento")
    @ApiResponse(responseCode = "201", description = "Equipamento cadastrado e disponível", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "409", description = "Patrimônio já cadastrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @PostMapping
    public ResponseEntity<ResultModel<Equipamento>> cadastrar(
            @RequestBody @Valid EquipamentoRegisterRequest equipamento
    ){
        return equipamentoService.cadastrar(equipamento);
    }

    @Operation(summary = "Atualizar nome e tipo do equipamento")
    @ApiResponse(responseCode = "200", description = "Equipamento atualizado", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Equipamento não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @PutMapping("/{id}")
    public ResponseEntity<ResultModel<Equipamento>> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EquipamentoUpdateRequest dados
    ){
        return equipamentoService.atualizar(id, dados);
    }

    @Operation(summary = "Solicitar exclusão de equipamento")
    @ApiResponse(responseCode = "204", description = "Sem conteúdo; a implementação atual não exclui o registro do banco", content = @Content)
    @ApiResponse(responseCode = "400", description = "ID inválido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "404", description = "Equipamento não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @ApiResponse(responseCode = "409", description = "Equipamento tem empréstimos registrados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroResposta.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<ResultModel<Void>> remover(
            @PathVariable Long id
    ){
        return equipamentoService.remover(id);
    }
}
