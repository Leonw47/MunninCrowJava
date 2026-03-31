package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.*;
import br.com.munnincrow.api.model.*;
import br.com.munnincrow.api.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propostas")
public class PropostaController {

    private final PropostaService propostaService;
    private final CampoPropostaService campoService;
    private final EditalService editalService;

    public PropostaController(PropostaService propostaService,
                              CampoPropostaService campoService,
                              EditalService editalService) {
        this.propostaService = propostaService;
        this.campoService = campoService;
        this.editalService = editalService;
    }

    // ---------------------------------------------------------
    // CRIAR PROPOSTA PARA UM EDITAL
    // ---------------------------------------------------------
    @PostMapping("/criar")
    public ResponseEntity<PropostaResponse> criar(@RequestBody PropostaRequest req,
                                                  @RequestAttribute("usuario") User usuario) {

        Edital edital = editalService.buscarPorId(req.editalId);
        Proposta proposta = propostaService.criarProposta(usuario, edital);

        return ResponseEntity.ok(toResponse(proposta));
    }

    // ---------------------------------------------------------
    // LISTAR PROPOSTAS DO USUÁRIO
    // ---------------------------------------------------------
    @GetMapping
    public List<PropostaResponse> listar(@RequestAttribute("usuario") User usuario) {
        return propostaService.listarPorUsuario(usuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ---------------------------------------------------------
    // BUSCAR PROPOSTA POR ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<PropostaResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(propostaService.buscarPorId(id)));
    }

    // ---------------------------------------------------------
    // LISTAR CAMPOS DA PROPOSTA
    // ---------------------------------------------------------
    @GetMapping("/{id}/campos")
    public List<CampoPropostaResponse> listarCampos(@PathVariable Long id) {
        return campoService.listarPorProposta(id)
                .stream()
                .map(this::toCampoResponse)
                .toList();
    }

    // ---------------------------------------------------------
    // AUTO-SAVE DE CAMPO
    // ---------------------------------------------------------
    @PutMapping("/{id}/campo")
    public ResponseEntity<CampoPropostaResponse> salvarCampo(@PathVariable Long id,
                                                             @RequestBody CampoPropostaRequest req) {

        CampoProposta campo = campoService.salvarValorCampo(id, req.nomeCampo, req.valor);
        return ResponseEntity.ok(toCampoResponse(campo));
    }

    // ---------------------------------------------------------
    // MARCAR CAMPO COMO CONCLUÍDO
    // ---------------------------------------------------------
    @PutMapping("/campo/{campoId}/concluir")
    public ResponseEntity<CampoPropostaResponse> concluir(@PathVariable Long campoId,
                                                          @RequestParam boolean concluido) {

        CampoProposta campo = campoService.marcarConcluido(campoId, concluido);
        return ResponseEntity.ok(toCampoResponse(campo));
    }

    // ---------------------------------------------------------
    // CONVERSÃO DTOs
    // ---------------------------------------------------------
    private PropostaResponse toResponse(Proposta p) {
        PropostaResponse r = new PropostaResponse();
        r.id = p.getId();
        r.editalId = p.getEdital().getId();
        r.editalTitulo = p.getEdital().getTitulo();
        r.titulo = p.getTitulo();
        r.status = p.getStatus();
        r.dataCriacao = p.getDataCriacao();
        r.dataAtualizacao = p.getDataAtualizacao();

        r.campos = p.getCampos()
                .stream()
                .map(this::toCampoResponse)
                .toList();

        return r;
    }

    private CampoPropostaResponse toCampoResponse(CampoProposta c) {
        CampoPropostaResponse r = new CampoPropostaResponse();
        r.id = c.getId();
        r.nomeCampo = c.getNomeCampo();
        r.valor = c.getValor();
        r.concluido = c.isConcluido();
        return r;
    }

    @PutMapping("/{id}/campo/gerar")
    public ResponseEntity<CampoPropostaResponse> gerarComIA(
            @PathVariable Long id,
            @RequestParam String nomeCampo) {

        String texto = IAPropostaService.gerarTextoParaCampo(id, nomeCampo);
        CampoProposta campo = campoService.salvarValorGerado(id, nomeCampo, texto);

        return ResponseEntity.ok(toCampoResponse(campo));
    }
}