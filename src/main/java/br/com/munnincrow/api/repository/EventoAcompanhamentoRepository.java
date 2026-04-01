package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.AcompanhamentoProposta;
import br.com.munnincrow.api.model.EventoAcompanhamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoAcompanhamentoRepository extends JpaRepository<EventoAcompanhamento, Long> {
    List<EventoAcompanhamento> findByAcompanhamentoOrderByDataDesc(AcompanhamentoProposta acompanhamento);
}