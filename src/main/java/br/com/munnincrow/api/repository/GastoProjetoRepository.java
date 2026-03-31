package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.GastoProjeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GastoProjetoRepository extends JpaRepository<GastoProjeto, Long> {
}