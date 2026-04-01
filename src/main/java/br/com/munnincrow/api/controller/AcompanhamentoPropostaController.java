package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.AcompanhamentoResponse;
import br.com.munnincrow.api.dto.EventoAcompanhamentoResponse;
import br.com.munnincrow.api.model.AcompanhamentoProposta;
import br.com.munnincrow.api.model.EventoAcompanhamento;
import br.com.munnincrow.api.service.AcompanhamentoPropostaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/acompanhamento")
public class AcompanhamentoPropostaController {

    private final AcompanhamentoPropostaService service;

    public AcompanhamentoPropostaController(AcompanhamentoPropostaService service) {
        this.service = service;
    }

    @PutMapping("/{propostaId}/status")
    public ResponseEntity<AcompanhamentoResponse> atualizarStatus(
            @PathVariable Long propostaId,
            @RequestParam String status) {

        AcompanhamentoProposta acomp = service.atualizarStatus(propostaId, status);
        return ResponseEntity.ok(toResponse(acomp));
    }

    @GetMapping("/{propostaId}/eventos")
    public List<EventoAcompanhamentoResponse> listarEventos(@PathVariable Long propostaId) {
        return service.listarEventos(propostaId)
                .stream()
                .map(this::toEventoResponse)
                .toList();
    }

    private AcompanhamentoResponse toResponse(AcompanhamentoProposta a) {
        AcompanhamentoResponse r = new AcompanhamentoResponse();
        r.propostaId = a.getProposta().getId();
        r.statusAtual = a.getStatusAtual();
        r.ultimaAtualizacao = a.getUltimaAtualizacao();
        r.eventos = a.getEventos().stream().map(this::toEventoResponse).toList();
        return r;
    }

    private EventoAcompanhamentoResponse toEventoResponse(EventoAcompanhamento e) {
        EventoAcompanhamentoResponse r = new EventoAcompanhamentoResponse();
        r.data = e.getData();
        r.tipo = e.getTipo();
        r.descricao = e.getDescricao();
        return r;
    }
}
