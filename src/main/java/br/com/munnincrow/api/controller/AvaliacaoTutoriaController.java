package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.AvaliacaoTutoriaRequest;
import br.com.munnincrow.api.dto.AvaliacaoTutoriaResponse;
import br.com.munnincrow.api.model.AvaliacaoTutoria;
import br.com.munnincrow.api.service.AvaliacaoTutoriaService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoTutoriaController {

    private final AvaliacaoTutoriaService service;

    public AvaliacaoTutoriaController(AvaliacaoTutoriaService service) {
        this.service = service;
    }

    @PostMapping("/{solicitacaoId}")
    public AvaliacaoTutoriaResponse avaliar(
            @PathVariable Long solicitacaoId,
            @RequestParam Long empreendedorId,
            @RequestBody AvaliacaoTutoriaRequest req
    ) {
        return toResponse(service.avaliar(solicitacaoId, empreendedorId, req));
    }

    @GetMapping("/consultor/{consultorId}")
    public List<AvaliacaoTutoriaResponse> listarPorConsultor(@PathVariable Long consultorId) {
        return service.listarPorConsultor(consultorId).stream()
                .map(this::toResponse)
                .toList();
    }

    private AvaliacaoTutoriaResponse toResponse(AvaliacaoTutoria a) {
        AvaliacaoTutoriaResponse resp = new AvaliacaoTutoriaResponse();
        resp.id = a.getId();
        resp.solicitacaoId = a.getSolicitacao().getId();
        resp.consultorId = a.getConsultor().getId();
        resp.nota = a.getNota();
        resp.comentario = a.getComentario();
        resp.dataCriacao = a.getDataCriacao();
        return resp;
    }
}