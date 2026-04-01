package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.AlertaResponse;
import br.com.munnincrow.api.model.AlertaProposta;
import br.com.munnincrow.api.service.AlertaPropostaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaPropostaController {

    private final AlertaPropostaService service;

    public AlertaPropostaController(AlertaPropostaService service) {
        this.service = service;
    }

    @GetMapping("/{propostaId}")
    public List<AlertaResponse> listar(@PathVariable Long propostaId) {
        return service.listarAlertas(propostaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @PutMapping("/{alertaId}/lido")
    public ResponseEntity<Void> marcarComoLido(@PathVariable Long alertaId) {
        service.marcarComoLido(alertaId);
        return ResponseEntity.ok().build();
    }


    private AlertaResponse toResponse(AlertaProposta a) {
        AlertaResponse r = new AlertaResponse();
        r.id = a.getId();
        r.tipo = a.getTipo();
        r.mensagem = a.getMensagem();
        r.dataGeracao = a.getDataGeracao();
        r.lido = a.isLido();
        return r;
    }

}