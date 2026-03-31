package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.PropostaTutoria;
import br.com.munnincrow.api.model.enums.StatusPropostaTutoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PropostaTutoriaRepository extends JpaRepository<PropostaTutoria, Long>, JpaSpecificationExecutor<PropostaTutoria> {

    List<PropostaTutoria> findBySolicitacaoIdOrderByDataCriacaoAsc(Long solicitacaoId);

    List<PropostaTutoria> findBySolicitacaoEmpreendedorIdOrderByDataCriacaoDesc(Long empreendedorId);

    List<PropostaTutoria> findByAutorIdOrderByDataCriacaoDesc(Long autorId);

    long countByAutorIdAndStatus(Long autorId, StatusPropostaTutoria status);
}
