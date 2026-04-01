package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.AlertaProposta;
import br.com.munnincrow.api.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaPropostaRepository extends JpaRepository<AlertaProposta, Long> {
    List<AlertaProposta> findByPropostaOrderByDataGeracaoDesc(Proposta proposta);
}