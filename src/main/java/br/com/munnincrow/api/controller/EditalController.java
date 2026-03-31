package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.EditalRequest;
import br.com.munnincrow.api.dto.EditalResponse;
import br.com.munnincrow.api.dto.EstatisticaEstadoResponse;
import br.com.munnincrow.api.dto.EstatisticaCategoriaResponse;
import br.com.munnincrow.api.dto.EstatisticaAreaTematicaResponse;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.service.EditalImportService;
import br.com.munnincrow.api.service.EditalService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/editais")
public class EditalController {

    private static final Logger logger = LoggerFactory.getLogger(EditalController.class);

    private final EditalService service;
    private final EditalImportService importService;

    public EditalController(EditalService service, EditalImportService importService) {
        this.service = service;
        this.importService = importService;
    }

    // ---------------------------------------------------------
    // IMPORTAÇÃO MANUAL (RF01)
    // ---------------------------------------------------------
    @PostMapping("/importar")
    public ResponseEntity<Map<String, Object>> importar() {
        logger.info("Importação manual iniciada via endpoint.");

        List<Edital> importados = importService.importarTodos();

        int novos = 0;
        int atualizados = 0;
        int erros = 0;

        for (Edital edital : importados) {
            try {
                boolean existe = service.buscarPorLinkOptional(edital.getLinkOficial()).isPresent();
                service.salvarImportado(edital);

                if (existe) atualizados++;
                else novos++;

            } catch (Exception e) {
                erros++;
                logger.warn("Falha ao salvar edital '{}' durante importação manual: {}", edital.getTitulo(), e.getMessage());
            }
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("novos", novos);
        resposta.put("atualizados", atualizados);
        resposta.put("erros", erros);
        resposta.put("totalProcessados", novos + atualizados);

        return ResponseEntity.ok(resposta);
    }

    // ---------------------------------------------------------
    // CONSULTAS (RF03)
    // ---------------------------------------------------------
    @GetMapping
    public Page<EditalResponse> listar(Pageable pageable) {
        return service.listar(pageable).map(this::toResponse);
    }

    @GetMapping("/estado/{uf}")
    public Page<EditalResponse> listarPorEstado(@PathVariable String uf, Pageable pageable) {
        return service.listarPorEstado(uf, pageable).map(this::toResponse);
    }

    @GetMapping("/orgao/{nome}")
    public Page<EditalResponse> listarPorOrgao(@PathVariable String nome, Pageable pageable) {
        return service.listarPorOrgao(nome, pageable).map(this::toResponse);
    }

    @GetMapping("/busca")
    public Page<EditalResponse> buscaAvancada(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String orgao,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String areaTematica,
            @RequestParam(required = false) String status,
            Pageable pageable
    ) {
        return service.buscaAvancada(estado, orgao, categoria, areaTematica, status, pageable)
                .map(this::toResponse);
    }

    @GetMapping("/buscar")
    public Page<EditalResponse> buscarTexto(@RequestParam String texto, Pageable pageable) {
        return service.buscaTexto(texto, pageable).map(this::toResponse);
    }

    @GetMapping("/autocomplete")
    public List<EditalResponse> autocomplete(@RequestParam String texto) {
        Pageable limit = PageRequest.of(0, 5);
        return service.buscaTexto(texto, limit).map(this::toResponse).toList();
    }

    @GetMapping("/estatisticas/por-estado")
    public List<EstatisticaEstadoResponse> estatisticasPorEstado() {
        return service.estatisticasPorEstado();
    }

    @GetMapping("/estatisticas/por-categoria")
    public List<EstatisticaCategoriaResponse> estatisticasPorCategoria() {
        return service.estatisticasPorCategoria();
    }

    @GetMapping("/estatisticas/por-area-tematica")
    public List<EstatisticaAreaTematicaResponse> estatisticasPorAreaTematica() {
        return service.estatisticasPorAreaTematica();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditalResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(service.buscarPorId(id)));
    }

    // ---------------------------------------------------------
    // CRUD MANUAL (RF02)
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<EditalResponse> salvar(@Valid @RequestBody EditalRequest dto) {
        Edital salvo = service.criar(toEntity(dto));
        return ResponseEntity.status(201).body(toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditalResponse> atualizar(@PathVariable Long id, @Valid @RequestBody EditalRequest dto) {
        Edital atualizado = service.atualizar(id, toEntity(dto));
        return ResponseEntity.ok(toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------------
    // CONVERSÃO DTO <-> ENTIDADE
    // ---------------------------------------------------------
    private Edital toEntity(EditalRequest dto) {
        Edital edital = new Edital();
        edital.setTitulo(dto.titulo);
        edital.setDescricaoCurta(dto.descricaoCurta);
        edital.setOrgao(dto.orgao);
        edital.setEstado(dto.estado);
        edital.setAreaTematica(dto.areaTematica);
        edital.setCategoria(dto.categoria);
        edital.setDataAbertura(dto.dataAbertura);
        edital.setDataEncerramento(dto.dataEncerramento);
        edital.setLinkOficial(dto.link);
        edital.setValorMaximo(dto.valorMaximo);
        edital.setObjetivo(dto.objetivo);
        edital.setPublicoAlvo(dto.publicoAlvo);
        return edital;
    }

    private EditalResponse toResponse(Edital edital) {
        EditalResponse resp = new EditalResponse();
        resp.id = edital.getId();
        resp.titulo = edital.getTitulo();
        resp.descricaoCurta = edital.getDescricaoCurta();
        resp.orgao = edital.getOrgao();
        resp.estado = edital.getEstado();
        resp.areaTematica = edital.getAreaTematica();
        resp.areaTematicaReal = edital.getAreaTematicaReal();
        resp.categoria = edital.getCategoria();
        resp.dataAbertura = edital.getDataAbertura();
        resp.dataEncerramento = edital.getDataEncerramento();
        resp.link = edital.getLinkOficial();
        resp.valorMaximo = edital.getValorMaximo();
        resp.objetivo = edital.getObjetivo();
        resp.publicoAlvo = edital.getPublicoAlvo();
        resp.status = edital.getStatus();
        resp.dataImportacao = edital.getDataImportacao();
        return resp;
    }
}