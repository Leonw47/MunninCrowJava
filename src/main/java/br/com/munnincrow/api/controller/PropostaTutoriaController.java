package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.PropostaTutoriaRequest;
import br.com.munnincrow.api.dto.PropostaTutoriaResponse;
import br.com.munnincrow.api.model.PropostaTutoria;
import br.com.munnincrow.api.model.enums.StatusPropostaTutoria;
import br.com.munnincrow.api.model.enums.StatusSolicitacaoTutoria;
import br.com.munnincrow.api.model.enums.TipoSolicitacao;
import br.com.munnincrow.api.service.PropostaTutoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/propostas")
public class PropostaTutoriaController {

    private final PropostaTutoriaService service;

    public PropostaTutoriaController(PropostaTutoriaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PropostaTutoriaRequest dto) {
        try {
            PropostaTutoria proposta = new PropostaTutoria();
            proposta.setValor(dto.valor);
            proposta.setDescricao(dto.descricao);

            var solicitacao = service.buscarSolicitacao(dto.solicitacaoId);
            if (solicitacao == null) {
                return ResponseEntity.badRequest().body("Solicitação não encontrada.");
            }

            // Bloqueios adicionais
            if (solicitacao.getStatus() == StatusSolicitacaoTutoria.FECHADA)
                return ResponseEntity.badRequest().body("A solicitação já foi fechada.");

            if (solicitacao.getStatus() == StatusSolicitacaoTutoria.CANCELADA)
                return ResponseEntity.badRequest().body("A solicitação foi cancelada.");

            if (solicitacao.getStatus() == StatusSolicitacaoTutoria.EXPIRADA)
                return ResponseEntity.badRequest().body("A solicitação expirou.");

            // Regra: solicitação DIRETA → apenas o consultor indicado pode enviar
            if (solicitacao.getTipo() == TipoSolicitacao.DIRETA) {
                if (solicitacao.getConsultor() == null ||
                        !solicitacao.getConsultor().getId().equals(dto.autorId)) {
                    return ResponseEntity.status(403)
                            .body("A solicitação é direta. Apenas o consultor indicado pode enviar propostas.");
                }
            }

            PropostaTutoria salva = service.criar(proposta, dto.solicitacaoId, dto.autorId);
            return ResponseEntity.status(201).body(toResponse(salva));

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/solicitacao/{id}")
    public List<PropostaTutoriaResponse> listar(@PathVariable Long id) {
        return service.listarPorSolicitacao(id)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam Long autorId
    ) {
        try {
            var novoStatus = StatusPropostaTutoria.valueOf(status.toUpperCase());

            var proposta = service.buscarPorId(id);
            if (proposta == null) {
                return ResponseEntity.notFound().build();
            }

            var solicitacao = proposta.getSolicitacao();

            // Bloqueios adicionais
            if (solicitacao.getStatus() == StatusSolicitacaoTutoria.FECHADA)
                return ResponseEntity.badRequest().body("A solicitação já foi fechada.");

            if (solicitacao.getStatus() == StatusSolicitacaoTutoria.CANCELADA)
                return ResponseEntity.badRequest().body("A solicitação foi cancelada.");

            if (solicitacao.getStatus() == StatusSolicitacaoTutoria.EXPIRADA)
                return ResponseEntity.badRequest().body("A solicitação expirou.");

            // Regra: apenas o empreendedor pode ACEITAR
            if (novoStatus == StatusPropostaTutoria.ACEITA &&
                    !solicitacao.getEmpreendedor().getId().equals(autorId)) {
                return ResponseEntity.status(403)
                        .body("Somente o empreendedor pode aceitar uma proposta.");
            }

            // Regra: apenas o autor pode RECUSAR
            if (novoStatus == StatusPropostaTutoria.RECUSADA &&
                    !proposta.getAutor().getId().equals(autorId)) {
                return ResponseEntity.status(403)
                        .body("Somente o autor da proposta pode recusá-la.");
            }

            var atualizado = service.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(toResponse(atualizado));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Status inválido.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private PropostaTutoriaResponse toResponse(PropostaTutoria p) {
        PropostaTutoriaResponse resp = new PropostaTutoriaResponse();
        resp.id = p.getId();
        resp.valor = p.getValor();
        resp.descricao = p.getDescricao();
        resp.dataCriacao = p.getDataCriacao();
        resp.status = p.getStatus().name();
        resp.autorId = p.getAutor().getId();
        resp.solicitacaoId = p.getSolicitacao().getId();
        return resp;
    }

    @GetMapping("/filtrar")
    public Page<PropostaTutoriaResponse> filtrar(
            @RequestParam(required = false) StatusPropostaTutoria status,
            @RequestParam(required = false) Long autorId,
            @RequestParam(required = false) Long solicitacaoId,
            Pageable pageable
    ) {
        return service.buscarFiltrado(status, autorId, solicitacaoId, pageable)
                .map(this::toResponse);
    }
}