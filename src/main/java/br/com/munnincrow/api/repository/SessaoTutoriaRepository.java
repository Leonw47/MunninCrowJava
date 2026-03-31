package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.SessaoTutoria;
import br.com.munnincrow.api.model.enums.StatusSessao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface SessaoTutoriaRepository extends JpaRepository<SessaoTutoria, Long> {

    List<SessaoTutoria> findByConsultorId(Long consultorId);

    boolean existsByConsultorIdAndInicioLessThanEqualAndFimGreaterThanEqual(
            Long consultorId,
            LocalDateTime fim,
            LocalDateTime inicio
    );

    List<SessaoTutoria> findByEmpreendedorIdAndInicioAfterOrderByInicioAsc(Long empreendedorId, LocalDateTime now);

    List<SessaoTutoria> findByConsultorIdAndInicioAfterOrderByInicioAsc(Long consultorId, LocalDateTime now);

    long countByConsultorIdAndStatus(Long consultorId, StatusSessao status);
}
