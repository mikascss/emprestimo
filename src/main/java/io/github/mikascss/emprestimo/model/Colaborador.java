package io.github.mikascss.emprestimo.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Identificador gerado no cadastro")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    @Schema(description = "Nome do colaborador", example = "Maria Silva")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(nullable = false, unique = true)
    @Schema(description = "E-mail único do colaborador", example = "maria@example.com")
    private String email;

    @Schema(description = "Cargo do colaborador", example = "Analista")
    private String cargo;
}
