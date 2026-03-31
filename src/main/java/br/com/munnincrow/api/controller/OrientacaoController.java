package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.OrientacaoRequest;
import br.com.munnincrow.api.dto.OrientacaoResponse;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.Orientacao;
import br.com.munnincrow.api.service.EditalService;
import br.com.munnincrow.api.service.OrientacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orientacoes")
public class OrientacaoController {

    private final OrientacaoService service;
    private final EditalService editalService;

    public OrientacaoController(OrientacaoService service, EditalService editalService) {
        this.service = service;
        this.editalService = editalService;
    }

    @GetMapping("/edital/{editalId}")
    public List<OrientacaoResponse> listarPorEdital(@PathVariable Long editalId) {
        return service.listarPorEdital(editalId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<OrientacaoResponse> salvar(@Valid @RequestBody OrientacaoRequest dto) {
        Edital edital = editalService.buscarPorId(dto.editalId);

        Orientacao orientacao = new Orientacao();
        orientacao.setTitulo(dto.titulo);
        orientacao.setTipo(dto.tipo);
        orientacao.setUrl(dto.url);
        orientacao.setEdital(edital);

        Orientacao salvo = service.salvar(orientacao);
        return ResponseEntity.ok(toResponse(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removido = service.deletar(id);
        if (!removido) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    private OrientacaoResponse toResponse(Orientacao orientacao) {
        OrientacaoResponse resp = new OrientacaoResponse();
        resp.id = orientacao.getId();
        resp.titulo = orientacao.getTitulo();
        resp.tipo = orientacao.getTipo();
        resp.url = orientacao.getUrl();
        resp.editalId = orientacao.getEdital().getId();
        return resp;
    }
}