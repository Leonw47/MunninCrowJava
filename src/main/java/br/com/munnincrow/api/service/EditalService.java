package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.EstatisticaAreaTematicaResponse;
import br.com.munnincrow.api.dto.EstatisticaCategoriaResponse;
import br.com.munnincrow.api.dto.EstatisticaEstadoResponse;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.enums.FonteImportacao;
import br.com.munnincrow.api.model.enums.OrgaoEdital;
import br.com.munnincrow.api.model.enums.StatusEdital;
import br.com.munnincrow.api.repository.EditalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EditalService {

    private final EditalRepository editalRepo;

    public EditalService(EditalRepository editalRepo) {
        this.editalRepo = editalRepo;
    }

    // ---------------------------------------------------------
    // CRUD MANUAL (RF02)
    // ---------------------------------------------------------
    public Edital criar(Edital edital) {
        validarEdital(edital);
        verificarDuplicacao(edital);
        edital.setFonte(FonteImportacao.MANUAL);
        return editalRepo.save(edital);
    }

    public Edital atualizar(Long id, Edital dados) {
        Edital existente = buscarPorId(id);

        existente.setTitulo(dados.getTitulo());
        existente.setDescricaoCurta(dados.getDescricaoCurta());
        existente.setOrgao(dados.getOrgao());
        existente.setEstado(dados.getEstado());
        existente.setLinkOficial(dados.getLinkOficial());
        existente.setDataAbertura(dados.getDataAbertura());
        existente.setDataEncerramento(dados.getDataEncerramento());
        existente.setAreaTematica(dados.getAreaTematica());
        existente.setCategoria(dados.getCategoria());
        existente.setStatus(calcularStatus(dados.getDataAbertura(), dados.getDataEncerramento()));

        return editalRepo.save(existente);
    }

    public void deletar(Long id) {
        editalRepo.deleteById(id);
    }

    // ---------------------------------------------------------
    // CONSULTAS (RF03)
    // ---------------------------------------------------------
    public Edital buscarPorId(Long id) {
        return editalRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Edital não encontrado."));
    }

    public Page<Edital> listar(Pageable pageable) {
        return editalRepo.findAll(pageable);
    }

    public Page<Edital> listarPorEstado(String estado, Pageable pageable) {
        return editalRepo.findByEstado(estado, pageable);
    }

    public Page<Edital> listarPorOrgao(String orgao, Pageable pageable) {
        OrgaoEdital o = OrgaoEdital.valueOf(orgao.toUpperCase());
        return editalRepo.findByOrgao(o, pageable);
    }

    public Page<Edital> buscaTexto(String texto, Pageable pageable) {
        return editalRepo.findByTituloContainingIgnoreCase(texto, pageable);
    }

    public Page<Edital> buscaAvancada(
            String estado,
            String orgao,
            String categoria,
            String areaTematica,
            String status,
            Pageable pageable
    ) {
        OrgaoEdital orgaoEnum = orgao != null ? OrgaoEdital.valueOf(orgao.toUpperCase()) : null;
        StatusEdital statusEnum = status != null ? StatusEdital.valueOf(status.toUpperCase()) : null;

        return editalRepo.buscaAvancada(
                estado,
                orgaoEnum,
                categoria,
                areaTematica,
                statusEnum,
                pageable
        );
    }

    // ---------------------------------------------------------
    // APOIO AO IMPORTADOR (RF01)
    // ---------------------------------------------------------
    public Edital salvarImportado(Edital edital) {
        validarEdital(edital);
        verificarDuplicacao(edital);
        return editalRepo.save(edital);
    }

    // ---------------------------------------------------------
    // ESTATÍSTICAS (RF03)
    // ---------------------------------------------------------
    public List<EstatisticaEstadoResponse> estatisticasPorEstado() {
        return editalRepo.estatisticasPorEstado();
    }

    public List<EstatisticaCategoriaResponse> estatisticasPorCategoria() {
        return editalRepo.estatisticasPorCategoria();
    }

    public List<EstatisticaAreaTematicaResponse> estatisticasPorAreaTematica() {
        return editalRepo.estatisticasPorAreaTematica();
    }

    // ---------------------------------------------------------
    // REGRAS DE NEGÓCIO
    // ---------------------------------------------------------
    private void validarEdital(Edital edital) {
        if (edital.getTitulo() == null || edital.getTitulo().isBlank())
            throw new IllegalArgumentException("Título é obrigatório.");

        if (edital.getLinkOficial() == null || edital.getLinkOficial().isBlank())
            throw new IllegalArgumentException("Link oficial é obrigatório.");

        if (edital.getDataAbertura() == null || edital.getDataEncerramento() == null)
            throw new IllegalArgumentException("Datas de abertura e encerramento são obrigatórias.");
    }

    private void verificarDuplicacao(Edital edital) {
        editalRepo.findByLinkOficial(edital.getLinkOficial())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("Já existe um edital com este link.");
                });

        editalRepo.findByTituloAndOrgao(edital.getTitulo(), edital.getOrgao())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("Já existe um edital com este título para este órgão.");
                });
    }

    public StatusEdital calcularStatus(LocalDate abertura, LocalDate encerramento) {
        LocalDate hoje = LocalDate.now();

        if (hoje.isBefore(abertura)) return StatusEdital.PREVISTO;
        if (hoje.isAfter(encerramento)) return StatusEdital.FECHADO;
        return StatusEdital.ABERTO;
    }
}