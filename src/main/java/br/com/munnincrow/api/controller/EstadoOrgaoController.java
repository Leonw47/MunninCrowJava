package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.EditalResponse;
import br.com.munnincrow.api.dto.EstadoOrgaoRequest;
import br.com.munnincrow.api.dto.EstadoOrgaoResponse;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.EstadoOrgao;
import br.com.munnincrow.api.service.EditalService;
import br.com.munnincrow.api.service.EstadoOrgaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


import java.util.List;

@RestController
@RequestMapping("/api/estado-orgao")
public class EstadoOrgaoController {

    private final EstadoOrgaoService service;
    private final EditalService editalService;

    public EstadoOrgaoController(EstadoOrgaoService service, EditalService editalService) {
        this.service = service;
        this.editalService = editalService;
    }

    @GetMapping
    public List<EstadoOrgaoResponse> listar() {
        return service.listar()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoOrgaoResponse> buscar(@PathVariable Long id) {
        EstadoOrgao eo = service.buscarPorId(id);
        if (eo == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(eo));
    }

    @GetMapping("/{id}/editais")
    public ResponseEntity<Page<EditalResponse>> listarEditais(
            @PathVariable Long id,
            Pageable pageable
    ) {
        EstadoOrgao eo = service.buscarPorId(id);
        if (eo == null) return ResponseEntity.notFound().build();

        Page<EditalResponse> pagina = editalService
                .listarPorEstadoOrgao(id, pageable)
                .map(this::toEditalResponse);

        return ResponseEntity.ok(pagina);
    }

    @PostMapping
    public ResponseEntity<EstadoOrgaoResponse> salvar(@Valid @RequestBody EstadoOrgaoRequest dto) {
        EstadoOrgao salvo = service.buscarOuCriar(dto.estado, dto.orgao);
        return ResponseEntity.ok(toResponse(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = service.deletar(id);
        if (!removido) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    private EstadoOrgaoResponse toResponse(EstadoOrgao eo) {
        EstadoOrgaoResponse resp = new EstadoOrgaoResponse();
        resp.id = eo.getId();
        resp.estado = eo.getEstado();
        resp.orgao = eo.getOrgao();
        return resp;
    }

    private EditalResponse toEditalResponse(Edital edital) {
        EditalResponse resp = new EditalResponse();
        resp.id = edital.getId();
        resp.titulo = edital.getTitulo();
        resp.orgao = edital.getOrgao();
        resp.areaTematica = edital.getAreaTematica();
        resp.categoria = edital.getCategoria();
        resp.dataAbertura = edital.getDataAbertura();
        resp.dataFechamento = edital.getDataFechamento();
        resp.link = edital.getLink();
        resp.status = edital.getStatus();
        resp.descricao = edital.getDescricao();
        resp.estadoOrgaoId = edital.getEstadoOrgao() != null
                ? edital.getEstadoOrgao().getId()
                : null;
        resp.dataImportacao = edital.getDataImportacao();
        return resp;
    }
}