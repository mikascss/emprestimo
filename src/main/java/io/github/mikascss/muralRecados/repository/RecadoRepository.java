package io.github.mikascss.muralRecados.repository;

import io.github.mikascss.muralRecados.model.Recado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecadoRepository extends JpaRepository<Recado, Long> {
}
