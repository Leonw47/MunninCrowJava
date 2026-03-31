package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.GastoProjetoRequest;
import br.com.munnincrow.api.dto.GastoProjetoResponse;
import br.com.munnincrow.api.model.AcompanhamentoProjeto;
import br.com.munnincrow.api.model.GastoProjeto;
import br.com.munnincrow.api.service.AcompanhamentoProjetoService;
import br.com.munnincrow.api.service.GastoProjetoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gastos")
public class GastoProjetoController {

    private final GastoProjetoService service;
    private final AcompanhamentoProjetoService acompanhamentoService;

    public GastoProjetoController(GastoProjetoService service, AcompanhamentoProjetoService acompanhamentoService) {
        this.service = service;
        this.acompanhamentoService = acompanhamentoService;
    }

    @GetMapping
    public List<GastoProjetoResponse> listar() {
        return service.listar()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoProjetoResponse> buscarPorId(@PathVariable Long id) {
        GastoProjeto gasto = service.buscarPorId(id);
        if (gasto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(gasto));
    }

    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody GastoProjetoRequest dto) {

        AcompanhamentoProjeto acompanhamento = acompanhamentoService.buscarPorId(dto.acompanhamentoId);
        if (acompanhamento == null) {
            return ResponseEntity.badRequest().body("Acompanhamento não encontrado.");
        }

        GastoProjeto gasto = new GastoProjeto();
        gasto.setCategoria(dto.categoria);
        gasto.setValor(dto.valor);
        gasto.setData(dto.data);
        gasto.setDescricao(dto.descricao);
        gasto.setAcompanhamentoProjeto(acompanhamento);

        GastoProjeto salvo = service.salvar(gasto);
        return ResponseEntity.status(201).body(toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody GastoProjetoRequest dto) {

        GastoProjeto dados = new GastoProjeto();
        dados.setCategoria(dto.categoria);
        dados.setValor(dto.valor);
        dados.setData(dto.data);
        dados.setDescricao(dto.descricao);

        GastoProjeto atualizado = service.atualizar(id, dados);
        if (atualizado == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = service.deletar(id);
        if (!removido) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    private GastoProjetoResponse toResponse(GastoProjeto gasto) {
        GastoProjetoResponse resp = new GastoProjetoResponse();
        resp.id = gasto.getId();
        resp.categoria = gasto.getCategoria();
        resp.valor = gasto.getValor();
        resp.data = gasto.getData();
        resp.descricao = gasto.getDescricao();
        resp.acompanhamentoId = gasto.getAcompanhamentoProjeto().getId();
        return resp;
    }
}