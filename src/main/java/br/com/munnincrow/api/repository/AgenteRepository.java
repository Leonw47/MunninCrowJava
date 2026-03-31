package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenteRepository extends JpaRepository<Agente, Long> {
}