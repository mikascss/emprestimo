package io.github.mikascss.tarefas.repository;

import io.github.mikascss.tarefas.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long>{
}
