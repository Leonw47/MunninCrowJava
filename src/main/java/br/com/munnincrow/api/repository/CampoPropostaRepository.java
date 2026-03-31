package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.CampoProposta;
import br.com.munnincrow.api.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampoPropostaRepository extends JpaRepository<CampoProposta, Long> {

    List<CampoProposta> findByProposta(Proposta proposta);

    Optional<CampoProposta> findByPropostaAndNomeCampo(Proposta proposta, String nomeCampo);
}