package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.PagamentoTutoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagamentoTutoriaRepository extends JpaRepository<PagamentoTutoria, Long> {
    Optional<PagamentoTutoria> findByPropostaId(Long propostaId);

    List<PagamentoTutoria> findByEmpreendedorIdOrderByDataCriacaoDesc(Long empreendedorId);

    List<PagamentoTutoria> findByConsultorIdOrderByDataCriacaoDesc(Long consultorId);
}