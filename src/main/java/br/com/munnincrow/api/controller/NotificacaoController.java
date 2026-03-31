package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.model.Notificacao;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.service.NotificacaoService;
import br.com.munnincrow.api.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final UserService userService;

    public NotificacaoController(NotificacaoService notificacaoService, UserService userService) {
        this.notificacaoService = notificacaoService;
        this.userService = userService;
    }

    private User usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.buscarPorEmail(email);
    }

    @GetMapping
    public List<Notificacao> listar() {
        return notificacaoService.listar(usuarioLogado());
    }

    @PostMapping("/gerar")
    public void gerar() {
        notificacaoService.gerarNotificacoes(usuarioLogado());
    }

    @PutMapping("/{id}/lida")
    public void marcarComoLida(@PathVariable Long id) {
        notificacaoService.marcarComoLida(id, usuarioLogado());
    }
}