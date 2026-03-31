package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.MensagemChatRequest;
import br.com.munnincrow.api.dto.MensagemChatResponse;
import br.com.munnincrow.api.model.MensagemChat;
import br.com.munnincrow.api.service.MensagemChatService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mensagens")
public class MensagemChatController {

    private final MensagemChatService service;

    public MensagemChatController(MensagemChatService service) {
        this.service = service;
    }

    // Lista mensagens raiz de um edital (threads principais)
    @GetMapping("/edital/{editalId}")
    public Page<MensagemChatResponse> listarPorEdital(
            @PathVariable Long editalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<MensagemChat> mensagens = service.listarPorEdital(editalId, PageRequest.of(page, size));
        return mensagens.map(this::toResponse);
    }

    // Lista respostas de uma mensagem
    @GetMapping("/{id}/respostas")
    public List<MensagemChatResponse> listarRespostas(@PathVariable Long id) {
        return service.listarRespostas(id)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Cria mensagem ou resposta
    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody MensagemChatRequest dto) {
        try {
            MensagemChat mensagem = new MensagemChat();
            mensagem.setConteudo(dto.conteudo);

            MensagemChat salvo = service.salvar(
                    mensagem,
                    dto.editalId,
                    dto.autorId,
                    dto.mensagemPaiId
            );

            return ResponseEntity.status(201).body(toResponse(salvo));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Exclui mensagem e suas respostas
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = service.deletar(id);
        if (!removido) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    private MensagemChatResponse toResponse(MensagemChat mensagem) {
        MensagemChatResponse resp = new MensagemChatResponse();
        resp.id = mensagem.getId();
        resp.conteudo = mensagem.getConteudo();
        resp.dataEnvio = mensagem.getDataEnvio();
        resp.autorId = mensagem.getAutor().getId();
        resp.editalId = mensagem.getEdital().getId();
        resp.mensagemPaiId = mensagem.getMensagemPai() != null ? mensagem.getMensagemPai().getId() : null;
        return resp;
    }
}