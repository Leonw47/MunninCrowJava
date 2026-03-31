package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.SolicitacaoTutoriaRequest;
import br.com.munnincrow.api.dto.SolicitacaoTutoriaResponse;
import br.com.munnincrow.api.model.SolicitacaoTutoria;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.enums.StatusSolicitacaoTutoria;
import br.com.munnincrow.api.model.enums.TipoSolicitacao;
import br.com.munnincrow.api.service.PropostaTutoriaService;
import br.com.munnincrow.api.service.SolicitacaoTutoriaService;
import br.com.munnincrow.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tutorias")
public class SolicitacaoTutoriaController {

    private final SolicitacaoTutoriaService service;
    private final UserService userService;
    private final PropostaTutoriaService propostaService;

    public SolicitacaoTutoriaController(
            SolicitacaoTutoriaService service,
            UserService userService,
            PropostaTutoriaService propostaService
    ) {
        this.service = service;
        this.userService = userService;
        this.propostaService = propostaService;
    }

    @GetMapping
    public List<SolicitacaoTutoriaResponse> listar() {
        return service.listar()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoTutoriaResponse> buscarPorId(@PathVariable Long id) {
        SolicitacaoTutoria solicitacao = service.buscarPorId(id);
        if (solicitacao == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(solicitacao));
    }

    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody SolicitacaoTutoriaRequest dto) {

        User empreendedor = userService.buscarPorId(dto.empreendedorId);
        if (empreendedor == null) {
            return ResponseEntity.badRequest().body("Empreendedor não encontrado.");
        }

        User consultor = null;
        if (dto.consultorId != null) {
            consultor = userService.buscarPorId(dto.consultorId);
            if (consultor == null) {
                return ResponseEntity.badRequest().body("Consultor não encontrado.");
            }
        }

        SolicitacaoTutoria solicitacao = new SolicitacaoTutoria();
        solicitacao.setTipo(dto.tipo);
        solicitacao.setDescricao(dto.descricao);
        solicitacao.setEmpreendedor(empreendedor);
        solicitacao.setConsultor(consultor);

        SolicitacaoTutoria salvo = service.salvar(solicitacao);
        return ResponseEntity.status(201).body(toResponse(salvo));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {

        StatusSolicitacaoTutoria novoStatus;
        try {
            novoStatus = StatusSolicitacaoTutoria.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Status inválido.");
        }

        SolicitacaoTutoria atualizado = service.atualizarStatus(id, novoStatus);
        if (atualizado == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = service.deletar(id);
        if (!removido) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    private SolicitacaoTutoriaResponse toResponse(SolicitacaoTutoria solicitacao) {
        SolicitacaoTutoriaResponse resp = new SolicitacaoTutoriaResponse();
        resp.id = solicitacao.getId();
        resp.tipo = solicitacao.getTipo();
        resp.status = solicitacao.getStatus();
        resp.descricao = solicitacao.getDescricao();
        resp.empreendedorId = solicitacao.getEmpreendedor().getId();
        resp.consultorId = solicitacao.getConsultor() != null ? solicitacao.getConsultor().getId() : null;
        resp.dataCriacao = solicitacao.getDataCriacao();
        return resp;
    }

    @GetMapping("/{id}/negociacao")
    public ResponseEntity<?> obterNegociacao(
            @PathVariable Long id,
            @RequestParam Long usuarioId
    ) {
        var negociacao = propostaService.montarNegociacao(id, usuarioId);
        if (negociacao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(negociacao);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(
            @PathVariable Long id,
            @RequestParam Long usuarioId
    ) {
        try {
            var atualizado = service.cancelar(id, usuarioId);
            if (atualizado == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(service.toResponse(atualizado));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<?> reabrir(
            @PathVariable Long id,
            @RequestParam Long usuarioId
    ) {
        try {
            var atualizado = service.reabrir(id, usuarioId);
            if (atualizado == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(service.toResponse(atualizado));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    public Page<SolicitacaoTutoriaResponse> filtrar(
            @RequestParam(required = false) StatusSolicitacaoTutoria status,
            @RequestParam(required = false) TipoSolicitacao tipo,
            @RequestParam(required = false) Long empreendedorId,
            @RequestParam(required = false) Long consultorId,
            @RequestParam(required = false) String texto,
            Pageable pageable
    ) {
        return service.buscarFiltrado(status, tipo, empreendedorId, consultorId, texto, pageable)
                .map(service::toResponse);
    }
}