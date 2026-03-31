package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.HistoricoInteracaoResponse;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.service.HistoricoInteracaoService;
import br.com.munnincrow.api.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historico")
public class HistoricoInteracaoController {

    private final HistoricoInteracaoService historicoService;
    private final UserService userService;

    public HistoricoInteracaoController(HistoricoInteracaoService historicoService, UserService userService) {
        this.historicoService = historicoService;
        this.userService = userService;
    }

    private User usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.buscarPorEmail(email);
    }

    @GetMapping
    public List<HistoricoInteracaoResponse> listar() {
        User usuario = usuarioLogado();
        return historicoService.listarHistorico(usuario);
    }
}