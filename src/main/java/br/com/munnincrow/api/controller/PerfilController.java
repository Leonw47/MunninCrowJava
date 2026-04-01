package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.PerfilResponse;
import br.com.munnincrow.api.dto.PerfilUpdateRequest;
import br.com.munnincrow.api.dto.UpdateProfileRequest;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    private final UserService userService;

    public PerfilController(UserService userService) {
        this.userService = userService;
    }

    private User usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.buscarPorEmail(email);
    }

    @GetMapping
    public PerfilResponse meuPerfil() {
        User u = usuarioLogado();

        PerfilResponse resp = new PerfilResponse();
        resp.id = u.getId();
        resp.nome = u.getNome();
        resp.email = u.getEmail();
        resp.segmento = u.getSegmento();
        resp.maturidade = u.getMaturidade();
        resp.faturamentoAnual = u.getFaturamentoAnual();

        return resp;
    }

    @PutMapping("/me/update")
    public PerfilResponse atualizar(@RequestBody UpdateProfileRequest req) {

        User u = userService.getUsuarioLogado();
        User atualizado = userService.atualizarPerfil(u, req);

        PerfilResponse resp = new PerfilResponse();
        resp.id = atualizado.getId();
        resp.nome = atualizado.getNome();
        resp.email = atualizado.getEmail();
        resp.segmento = atualizado.getSegmento();
        resp.maturidade = atualizado.getMaturidade();
        resp.faturamentoAnual = atualizado.getFaturamentoAnual();

        return resp;
    }
}