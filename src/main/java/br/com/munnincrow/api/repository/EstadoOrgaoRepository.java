package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.EstadoOrgao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoOrgaoRepository extends JpaRepository<EstadoOrgao, Long> {

    Optional<EstadoOrgao> findByEstadoAndOrgao(String estado, String orgao);
}