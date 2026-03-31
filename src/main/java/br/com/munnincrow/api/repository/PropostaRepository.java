package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.Proposta;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.Edital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    List<Proposta> findByUsuario(User usuario);

    List<Proposta> findByEdital(Edital edital);

    Optional<Proposta> findByUsuarioAndEdital(User usuario, Edital edital);
}