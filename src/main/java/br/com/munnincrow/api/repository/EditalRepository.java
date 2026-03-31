package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.dto.EstatisticaAreaTematicaResponse;
import br.com.munnincrow.api.dto.EstatisticaCategoriaResponse;
import br.com.munnincrow.api.dto.EstatisticaEstadoResponse;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.enums.OrgaoEdital;
import br.com.munnincrow.api.model.enums.StatusEdital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EditalRepository extends JpaRepository<Edital, Long> {

    Optional<Edital> findByLinkOficial(String linkOficial);

    Optional<Edital> findByTituloAndOrgao(String titulo, OrgaoEdital orgao);

    Page<Edital> findByEstado(String estado, Pageable pageable);

    Page<Edital> findByOrgao(OrgaoEdital orgao, Pageable pageable);

    Page<Edital> findByTituloContainingIgnoreCase(String texto, Pageable pageable);

    Page<Edital> findByStatus(StatusEdital status, Pageable pageable);

    @Query("""
        SELECT e FROM Edital e
        WHERE (:estado IS NULL OR e.estado = :estado)
          AND (:orgao IS NULL OR e.orgao = :orgao)
          AND (:categoria IS NULL OR e.categoria = :categoria)
          AND (:areaTematica IS NULL OR e.areaTematica = :areaTematica)
          AND (:status IS NULL OR e.status = :status)
        """)
    Page<Edital> buscaAvancada(
            String estado,
            OrgaoEdital orgao,
            String categoria,
            String areaTematica,
            StatusEdital status,
            Pageable pageable
    );

    @Query("""
        SELECT new br.com.munnincrow.api.dto.EstatisticaEstadoResponse(e.estado, COUNT(e))
        FROM Edital e GROUP BY e.estado
        """)
    List<EstatisticaEstadoResponse> estatisticasPorEstado();

    @Query("""
        SELECT new br.com.munnincrow.api.dto.EstatisticaCategoriaResponse(e.categoria, COUNT(e))
        FROM Edital e GROUP BY e.categoria
        """)
    List<EstatisticaCategoriaResponse> estatisticasPorCategoria();

    @Query("""
        SELECT new br.com.munnincrow.api.dto.EstatisticaAreaTematicaResponse(e.areaTematica, COUNT(e))
        FROM Edital e GROUP BY e.areaTematica
        """)
    List<EstatisticaAreaTematicaResponse> estatisticasPorAreaTematica();
}