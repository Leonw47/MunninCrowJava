package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.AgenteRequest;
import br.com.munnincrow.api.dto.AgenteResponse;
import br.com.munnincrow.api.model.Agente;
import br.com.munnincrow.api.service.AgenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agentes")
public class AgenteController {

    private final AgenteService service;

    public AgenteController(AgenteService service) {
        this.service = service;
    }

    @GetMapping
    public List<AgenteResponse> listar() {
        return service.listar()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenteResponse> buscarPorId(@PathVariable Long id) {
        Agente agente = service.buscarPorId(id);
        if (agente == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(agente));
    }

    @PostMapping
    public ResponseEntity<AgenteResponse> salvar(@Valid @RequestBody AgenteRequest dto) {
        Agente agente = toEntity(dto);
        Agente salvo = service.salvar(agente);
        return ResponseEntity.ok(toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgenteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody AgenteRequest dto) {
        Agente atualizado = service.atualizar(id, toEntity(dto));
        if (atualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = service.deletar(id);
        if (!removido) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    // Conversão DTO → Entidade
    private Agente toEntity(AgenteRequest dto) {
        Agente agente = new Agente();
        agente.setNome(dto.nome);
        agente.setTipo(dto.tipo);
        agente.setDescricao(dto.descricao);
        return agente;
    }

    // Conversão Entidade → DTO
    private AgenteResponse toResponse(Agente agente) {
        AgenteResponse resp = new AgenteResponse();
        resp.id = agente.getId();
        resp.nome = agente.getNome();
        resp.tipo = agente.getTipo();
        resp.descricao = agente.getDescricao();
        return resp;
    }
}