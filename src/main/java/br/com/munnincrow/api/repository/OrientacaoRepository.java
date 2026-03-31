package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.Orientacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrientacaoRepository extends JpaRepository<Orientacao, Long> {

    List<Orientacao> findByEditalId(Long editalId);
}