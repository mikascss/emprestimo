package io.github.mikascss.emprestimo.model;

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
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve ter 8 dígitos, sem traço")
    private String cep;

    @NotBlank(message = "O número é obrigatório")
    @Column(nullable = false)
    private String numero;

    private String logradouro,
                    bairro,
                    cidade,
                    uf;
}
