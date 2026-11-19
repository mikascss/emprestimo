package io.github.mikascss.emprestimo.repository;

import io.github.mikascss.emprestimo.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    boolean existsByEquipamentoId(Long equipamentoId);

    List<Emprestimo> findByColaboradorId(Long colaboradorId);

    List<Emprestimo> findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate data);
}
