package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.MensagemTutoriaRequest;
import br.com.munnincrow.api.dto.MensagemTutoriaResponse;
import br.com.munnincrow.api.model.MensagemTutoria;
import br.com.munnincrow.api.service.MensagemTutoriaService;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mensagens")
public class MensagemTutoriaController {

    private final MensagemTutoriaService service;

    public MensagemTutoriaController(MensagemTutoriaService service) {
        this.service = service;
    }

    @PostMapping("/{solicitacaoId}")
    public MensagemTutoriaResponse enviar(
            @PathVariable Long solicitacaoId,
            @RequestBody MensagemTutoriaRequest req
    ) {
        return toResponse(service.enviar(solicitacaoId, req.autorId, req.conteudo));
    }

    @GetMapping("/{solicitacaoId}")
    public Page<MensagemTutoriaResponse> listar(
            @PathVariable Long solicitacaoId,
            Pageable pageable
    ) {
        return service.listar(solicitacaoId, pageable)
                .map(this::toResponse);
    }

    @PatchMapping("/{id}/lida")
    public void marcarComoLida(@PathVariable Long id) {
        service.marcarComoLida(id);
    }

    private MensagemTutoriaResponse toResponse(MensagemTutoria m) {
        MensagemTutoriaResponse resp = new MensagemTutoriaResponse();
        resp.id = m.getId();
        resp.autorId = m.getAutor().getId();
        resp.solicitacaoId = m.getSolicitacao().getId();
        resp.conteudo = m.getConteudo();
        resp.dataCriacao = m.getDataCriacao();
        resp.lida = m.isLida();
        return resp;
    }
}