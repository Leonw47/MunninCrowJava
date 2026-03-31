package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.HistoricoTutoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface HistoricoTutoriaRepository extends JpaRepository<HistoricoTutoria, Long> {

    List<HistoricoTutoria> findBySolicitacaoIdOrderByDataAsc(Long solicitacaoId);

    List<HistoricoTutoria> findByUsuarioIdOrderByDataDesc(Long usuarioId);
}