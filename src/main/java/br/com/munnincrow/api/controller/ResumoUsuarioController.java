package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.ResumoUsuarioResponse;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.service.ResumoUsuarioService;
import br.com.munnincrow.api.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumo")
public class ResumoUsuarioController {

    private final ResumoUsuarioService resumoService;
    private final UserService userService;

    public ResumoUsuarioController(ResumoUsuarioService resumoService, UserService userService) {
        this.resumoService = resumoService;
        this.userService = userService;
    }

    private User usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.buscarPorEmail(email);
    }

    @GetMapping
    public ResumoUsuarioResponse resumo() {
        return resumoService.gerarResumo(usuarioLogado());
    }
}