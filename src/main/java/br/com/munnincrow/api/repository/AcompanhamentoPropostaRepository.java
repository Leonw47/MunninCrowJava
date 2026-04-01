package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.AcompanhamentoProposta;
import br.com.munnincrow.api.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcompanhamentoPropostaRepository extends JpaRepository<AcompanhamentoProposta, Long> {
    Optional<AcompanhamentoProposta> findByProposta(Proposta proposta);
}
