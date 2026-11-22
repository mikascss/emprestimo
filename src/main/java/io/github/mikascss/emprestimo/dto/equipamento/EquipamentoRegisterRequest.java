package io.github.mikascss.emprestimo.dto.equipamento;

import io.github.mikascss.emprestimo.model.TipoEquipamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipamentoRegisterRequest(
        @NotBlank(message = "O patrimônio é obrigatório")
        String patrimonio,
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @NotNull(message = "O tipo é obrigatório")
        TipoEquipamento tipo) { }
