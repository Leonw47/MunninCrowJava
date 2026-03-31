package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.PagamentoRequest;
import br.com.munnincrow.api.dto.PagamentoResponse;
import br.com.munnincrow.api.model.PagamentoTutoria;
import br.com.munnincrow.api.service.PagamentoTutoriaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoTutoriaController {

    private final PagamentoTutoriaService service;

    public PagamentoTutoriaController(PagamentoTutoriaService service) {
        this.service = service;
    }

    @PostMapping("/{propostaId}")
    public PagamentoResponse iniciar(
            @PathVariable Long propostaId,
            @RequestBody PagamentoRequest req
    ) {
        return toResponse(service.iniciarPagamento(propostaId, req));
    }

    @PatchMapping("/{pagamentoId}/confirmar")
    public PagamentoResponse confirmar(@PathVariable Long pagamentoId) {
        return toResponse(service.confirmarPagamento(pagamentoId));
    }

    private PagamentoResponse toResponse(PagamentoTutoria p) {
        PagamentoResponse resp = new PagamentoResponse();
        resp.id = p.getId();
        resp.propostaId = p.getProposta().getId();
        resp.valor = p.getValor();
        resp.status = p.getStatus();
        resp.dataCriacao = p.getDataCriacao();
        resp.dataConfirmacao = p.getDataConfirmacao();
        return resp;
    }
}