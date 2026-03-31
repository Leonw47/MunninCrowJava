package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.Proposta;
import br.com.munnincrow.api.model.enums.StatusPropostaTutoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    List<Proposta> findByEditalId(Long editalId);
    List<Proposta> findByCriadoPorId(Long userId);
    long countByAutorIdAndStatus(Long autorId, StatusPropostaTutoria status);
}
