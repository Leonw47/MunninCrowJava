package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.MensagemTutoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MensagemTutoriaRepository extends JpaRepository<MensagemTutoria, Long> {

    Page<MensagemTutoria> findBySolicitacaoIdOrderByDataCriacaoAsc(Long solicitacaoId, Pageable pageable);
}