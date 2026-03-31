package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.PropostaCreateDTO;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.Proposta;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.service.EditalService;
import br.com.munnincrow.api.service.PropostaService;
import br.com.munnincrow.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private final PropostaService propostaService;
    private final UserService userService;
    private final EditalService editalService;

    public PropostaController(PropostaService propostaService,
                              UserService userService,
                              EditalService editalService) {
        this.propostaService = propostaService;
        this.userService = userService;
        this.editalService = editalService;
    }

    @GetMapping
    public List<Proposta> listar() {
        return propostaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proposta> buscarPorId(@PathVariable Long id) {
        Proposta p = propostaService.buscarPorId(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @GetMapping("/edital/{editalId}")
    public List<Proposta> listarPorEdital(@PathVariable Long editalId) {
        return propostaService.listarPorEdital(editalId);
    }

    @GetMapping("/usuario/{userId}")
    public List<Proposta> listarPorUsuario(@PathVariable Long userId) {
        return propostaService.listarPorUsuario(userId);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PropostaCreateDTO dto) {

        User user = userService.buscarPorId(dto.userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Edital edital = editalService.buscarPorId(dto.editalId);
        if (edital == null) {
            return ResponseEntity.badRequest().body("Edital não encontrado.");
        }

        Proposta proposta = propostaService.criar(dto, user, edital);
        return ResponseEntity.status(201).body(proposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Proposta dados) {
        Proposta atualizada = propostaService.atualizar(id, dados);
        return atualizada != null ? ResponseEntity.ok(atualizada) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        boolean removido = propostaService.deletar(id);
        return removido ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}