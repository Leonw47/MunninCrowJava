package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.AcompanhamentoProjetoRequest;
import br.com.munnincrow.api.dto.AcompanhamentoProjetoResponse;
import br.com.munnincrow.api.model.AcompanhamentoProjeto;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.service.AcompanhamentoProjetoService;
import br.com.munnincrow.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acompanhamentos")
public class AcompanhamentoProjetoController {

    private final AcompanhamentoProjetoService service;
    private final UserService userService;

    public AcompanhamentoProjetoController(AcompanhamentoProjetoService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public List<AcompanhamentoProjetoResponse> listar() {
        return service.listar()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcompanhamentoProjetoResponse> buscarPorId(@PathVariable Long id) {
        AcompanhamentoProjeto projeto = service.buscarPorId(id);
        if (projeto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(projeto));
    }

    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody AcompanhamentoProjetoRequest dto) {

        User user = userService.buscarPorId(dto.criadoPorId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário criador não encontrado.");
        }

        AcompanhamentoProjeto projeto = new AcompanhamentoProjeto();
        projeto.setTitulo(dto.titulo);
        projeto.setDataInicio(dto.dataInicio);
        projeto.setDataFim(dto.dataFim);
        projeto.setStatus(dto.status);
        projeto.setCriadoPor(user);

        AcompanhamentoProjeto salvo = service.salvar(projeto);
        return ResponseEntity.status(201).body(toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody AcompanhamentoProjetoRequest dto) {

        AcompanhamentoProjeto dados = new AcompanhamentoProjeto();
        dados.setTitulo(dto.titulo);
        dados.setDataInicio(dto.dataInicio);
        dados.setDataFim(dto.dataFim);
        dados.setStatus(dto.status);

        AcompanhamentoProjeto atualizado = service.atualizar(id, dados);
        if (atualizado == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = service.deletar(id);
        if (!removido) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    private AcompanhamentoProjetoResponse toResponse(AcompanhamentoProjeto projeto) {
        AcompanhamentoProjetoResponse resp = new AcompanhamentoProjetoResponse();
        resp.id = projeto.getId();
        resp.titulo = projeto.getTitulo();
        resp.dataInicio = projeto.getDataInicio();
        resp.dataFim = projeto.getDataFim();
        resp.status = projeto.getStatus();
        resp.criadoPorId = projeto.getCriadoPor().getId();
        return resp;
    }
}