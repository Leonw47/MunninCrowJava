package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.AvaliacaoTutoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface AvaliacaoTutoriaRepository extends JpaRepository<AvaliacaoTutoria, Long> {

    Optional<AvaliacaoTutoria> findBySolicitacaoId(Long solicitacaoId);

    List<AvaliacaoTutoria> findByConsultorId(Long consultorId);

    List<AvaliacaoTutoria> findBySolicitacaoEmpreendedorId(Long empreendedorId);
}