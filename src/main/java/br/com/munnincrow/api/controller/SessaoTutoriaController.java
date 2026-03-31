package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.SessaoTutoriaRequest;
import br.com.munnincrow.api.dto.SessaoTutoriaResponse;
import br.com.munnincrow.api.model.SessaoTutoria;
import br.com.munnincrow.api.service.SessaoTutoriaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessoes")
public class SessaoTutoriaController {

    private final SessaoTutoriaService service;

    public SessaoTutoriaController(SessaoTutoriaService service) {
        this.service = service;
    }

    @PostMapping("/{solicitacaoId}")
    public SessaoTutoriaResponse agendar(
            @PathVariable Long solicitacaoId,
            @RequestBody SessaoTutoriaRequest req
    ) {
        return toResponse(service.agendar(solicitacaoId, req));
    }

    @PatchMapping("/{sessaoId}/cancelar")
    public void cancelar(
            @PathVariable Long sessaoId,
            @RequestParam Long usuarioId
    ) {
        service.cancelar(sessaoId, usuarioId);
    }

    private SessaoTutoriaResponse toResponse(SessaoTutoria s) {
        SessaoTutoriaResponse resp = new SessaoTutoriaResponse();
        resp.id = s.getId();
        resp.solicitacaoId = s.getSolicitacao().getId();
        resp.consultorId = s.getConsultor().getId();
        resp.empreendedorId = s.getEmpreendedor().getId();
        resp.inicio = s.getInicio();
        resp.fim = s.getFim();
        resp.status = s.getStatus();
        resp.observacoes = s.getObservacoes();
        return resp;
    }
}