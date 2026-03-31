package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.DisponibilidadeConsultor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibilidadeConsultorRepository extends JpaRepository<DisponibilidadeConsultor, Long> {
    List<DisponibilidadeConsultor> findByConsultorId(Long consultorId);
}
