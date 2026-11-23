package io.github.mikascss.emprestimo.dto.equipamento;

import io.swagger.v3.oas.annotations.media.Schema;

import io.github.mikascss.emprestimo.model.TipoEquipamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipamentoRegisterRequest(
        @NotBlank(message = "O patrimônio é obrigatório")
        @Schema(description = "Patrimônio único do equipamento", example = "PAT-001")
        String patrimonio,
        @NotBlank(message = "O nome é obrigatório")
        @Schema(description = "Nome do equipamento", example = "Notebook Dell")
        String nome,
        @NotNull(message = "O tipo é obrigatório")
        @Schema(description = "Tipo que determina o prazo do empréstimo", example = "NOTEBOOK")
        TipoEquipamento tipo) { }
