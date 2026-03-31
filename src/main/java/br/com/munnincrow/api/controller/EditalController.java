package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.*;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.FormularioEdital;
import br.com.munnincrow.api.service.EditalImportService;
import br.com.munnincrow.api.service.EditalService;
import br.com.munnincrow.api.service.ExtracaoFormularioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/editais")
public class EditalController {

    private static final Logger logger = LoggerFactory.getLogger(EditalController.class);

    private final EditalService editalService;
    private final EditalImportService importService;
    private final ExtracaoFormularioService extracaoFormularioService;

    public EditalController(
            EditalService editalService,
            EditalImportService importService,
            ExtracaoFormularioService extracaoFormularioService
    ) {
        this.editalService = editalService;
        this.importService = importService;
        this.extracaoFormularioService = extracaoFormularioService;
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
                boolean existe = editalService.buscarPorLinkOptional(edital.getLinkOficial()).isPresent();
                editalService.salvarImportado(edital);

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
    public Page<EditalResponse> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String areaTematica,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String busca,
            @PageableDefault(size = 20, sort = "dataEncerramento", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return editalService.listarFiltrado(status, areaTematica, categoria, busca, pageable)
                .map(this::toResponse);
    }

    @GetMapping("/por-estado")
    public Map<String, Long> porEstado() {
        return editalService.contarPorEstado();
    }

    @GetMapping("/orgao/{nome}")
    public Page<EditalResponse> listarPorOrgao(@PathVariable String nome, Pageable pageable) {
        return editalService.listarPorOrgao(nome, pageable).map(this::toResponse);
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
        return editalService.buscaAvancada(estado, orgao, categoria, areaTematica, status, pageable)
                .map(this::toResponse);
    }

    @GetMapping("/buscar")
    public Page<EditalResponse> buscarTexto(@RequestParam String texto, Pageable pageable) {
        return editalService.buscaTexto(texto, pageable).map(this::toResponse);
    }

    @GetMapping("/autocomplete")
    public List<EditalResponse> autocomplete(@RequestParam String texto) {
        Pageable limit = PageRequest.of(0, 5);
        return editalService.buscaTexto(texto, limit).map(this::toResponse).toList();
    }

    @GetMapping("/estatisticas/por-estado")
    public List<EstatisticaEstadoResponse> estatisticasPorEstado() {
        return editalService.estatisticasPorEstado();
    }

    @GetMapping("/estatisticas/por-categoria")
    public List<EstatisticaCategoriaResponse> estatisticasPorCategoria() {
        return editalService.estatisticasPorCategoria();
    }

    @GetMapping("/estatisticas/por-area-tematica")
    public List<EstatisticaAreaTematicaResponse> estatisticasPorAreaTematica() {
        return editalService.estatisticasPorAreaTematica();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditalResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(editalService.buscarPorId(id)));
    }

    // ---------------------------------------------------------
    // CRUD MANUAL (RF02)
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<EditalResponse> salvar(@Valid @RequestBody EditalRequest dto) {
        Edital salvo = editalService.criar(toEntity(dto));
        return ResponseEntity.status(201).body(toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditalResponse> atualizar(@PathVariable Long id, @Valid @RequestBody EditalRequest dto) {
        Edital atualizado = editalService.atualizar(id, toEntity(dto));
        return ResponseEntity.ok(toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        editalService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------------
    // EXTRAÇÃO DO FORMULÁRIO (RF06)
    // ---------------------------------------------------------
    @PostMapping("/{id}/formulario/extrair")
    public ResponseEntity<?> extrairFormulario(
            @PathVariable Long id,
            @RequestParam("arquivo") MultipartFile arquivo) {

        Edital edital = editalService.buscarPorId(id);
        FormularioEdital formulario = extracaoFormularioService.extrairFormulario(edital, arquivo);

        return ResponseEntity.ok("Formulário extraído com sucesso. Campos: " + formulario.getCampos().size());
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