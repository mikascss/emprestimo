package io.github.mikascss.emprestimo.repository;

import io.github.mikascss.emprestimo.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    boolean existsByEmail(String email);
}
