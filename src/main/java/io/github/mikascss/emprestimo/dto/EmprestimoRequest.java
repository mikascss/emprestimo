package io.github.mikascss.emprestimo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

public record EmprestimoRequest(
        @NotNull(message = "O equipamento é obrigatório")
        @Schema(description = "Identificador do equipamento", example = "1")
        Long equipamentoId,
        @NotNull(message = "O colaborador é obrigatório")
        @Schema(description = "Identificador do colaborador", example = "1")
        Long colaboradorId
) {
}
