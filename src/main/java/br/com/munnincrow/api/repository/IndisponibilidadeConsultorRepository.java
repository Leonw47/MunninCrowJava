package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.IndisponibilidadeConsultor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IndisponibilidadeConsultorRepository extends JpaRepository<IndisponibilidadeConsultor, Long> {
    List<IndisponibilidadeConsultor> findByConsultorId(Long consultorId);
}