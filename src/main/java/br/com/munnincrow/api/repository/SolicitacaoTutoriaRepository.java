package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.SolicitacaoTutoria;
import br.com.munnincrow.api.model.enums.StatusSolicitacaoTutoria;
import br.com.munnincrow.api.model.enums.TipoSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SolicitacaoTutoriaRepository extends JpaRepository<SolicitacaoTutoria, Long>, JpaSpecificationExecutor<SolicitacaoTutoria> {
    List<SolicitacaoTutoria> findByEmpreendedorIdOrderByDataCriacaoDesc(Long empreendedorId);

    List<SolicitacaoTutoria> findByStatusAndTipoAndConsultorIsNull(StatusSolicitacaoTutoria statusSolicitacaoTutoria, TipoSolicitacao tipoSolicitacao);
}