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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editais")
public class EditalController {

    private final EditalService service;
    private final EditalImportService importService;

    public EditalController(EditalService service, EditalImportService importService) {
        this.service = service;
        this.importService = importService;
    }

    @PostMapping("/importar")
    public ResponseEntity<List<EditalResponse>> importar() {
        List<Edital> importados = importService.importarTodos();

        // Salva cada edital importado
        importados.forEach(service::salvarImportado);

        return ResponseEntity.ok(
                importados.stream().map(this::toResponse).toList()
        );
    }

    @GetMapping
    public Page<EditalResponse> listar(Pageable pageable) {
        return service.listar(pageable)
                .map(this::toResponse);
    }

    @GetMapping("/estado/{uf}")
    public Page<EditalResponse> listarPorEstado(@PathVariable String uf, Pageable pageable) {
        return service.listarPorEstado(uf, pageable)
                .map(this::toResponse);
    }

    @GetMapping("/orgao/{nome}")
    public Page<EditalResponse> listarPorOrgao(@PathVariable String nome, Pageable pageable) {
        return service.listarPorOrgao(nome, pageable)
                .map(this::toResponse);
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
        return service.buscaTexto(texto, pageable)
                .map(this::toResponse);
    }

    @GetMapping("/autocomplete")
    public List<EditalResponse> autocomplete(@RequestParam String texto) {
        Pageable limit = PageRequest.of(0, 5);
        return service.buscaTexto(texto, limit)
                .map(this::toResponse)
                .toList();
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
        Edital edital = service.buscarPorId(id);
        return ResponseEntity.ok(toResponse(edital));
    }

    @PostMapping
    public ResponseEntity<EditalResponse> salvar(@Valid @RequestBody EditalRequest dto) {
        Edital edital = toEntity(dto);
        Edital salvo = service.criar(edital);
        return ResponseEntity.ok(toResponse(salvo));
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

    private Edital toEntity(EditalRequest dto) {
        Edital edital = new Edital();
        edital.setTitulo(dto.titulo);
        edital.setDescricaoCurta(dto.descricao);
        edital.setOrgao(dto.orgao);
        edital.setEstado(dto.estado);
        edital.setAreaTematica(dto.areaTematica);
        edital.setCategoria(dto.categoria);
        edital.setDataAbertura(dto.dataAbertura);
        edital.setDataEncerramento(dto.dataEncerramento);
        edital.setLinkOficial(dto.link);
        edital.setStatus(dto.status);
        return edital;
    }

    private EditalResponse toResponse(Edital edital) {
        EditalResponse resp = new EditalResponse();
        resp.id = edital.getId();
        resp.titulo = edital.getTitulo();
        resp.descricao = edital.getDescricaoCurta();
        resp.orgao = edital.getOrgao();
        resp.estado = edital.getEstado();
        resp.areaTematica = edital.getAreaTematica();
        resp.categoria = edital.getCategoria();
        resp.dataAbertura = edital.getDataAbertura();
        resp.dataEncerramento = edital.getDataEncerramento();
        resp.link = edital.getLinkOficial();
        resp.status = edital.getStatus();
        resp.dataImportacao = edital.getDataImportacao();
        return resp;
    }
}