package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.NotificacaoResponse;
import br.com.munnincrow.api.model.Notificacao;
import br.com.munnincrow.api.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService service;

    public NotificacaoController(NotificacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<NotificacaoResponse> listar(@RequestParam Long usuarioId) {
        return service.listar(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/nao-lidas")
    public List<NotificacaoResponse> listarNaoLidas(@RequestParam Long usuarioId) {
        return service.listarNaoLidas(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    @PatchMapping("/{id}/lida")
    public ResponseEntity<?> marcarComoLida(@PathVariable Long id) {
        Notificacao n = service.marcarComoLida(id);
        if (n == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(n));
    }

    @PatchMapping("/marcar-todas")
    public ResponseEntity<?> marcarTodasComoLidas(@RequestParam Long usuarioId) {
        service.marcarTodasComoLidas(usuarioId);
        return ResponseEntity.ok().build();
    }

    private NotificacaoResponse toResponse(Notificacao n) {
        NotificacaoResponse resp = new NotificacaoResponse();
        resp.id = n.getId();
        resp.mensagem = n.getMensagem();
        resp.dataCriacao = n.getDataCriacao();
        resp.lida = n.isLida();
        return resp;
    }
}