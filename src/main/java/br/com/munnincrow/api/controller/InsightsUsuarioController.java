package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.InsightsUsuarioResponse;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.service.InsightsUsuarioService;
import br.com.munnincrow.api.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insights")
public class InsightsUsuarioController {

    private final InsightsUsuarioService insightsService;
    private final UserService userService;

    public InsightsUsuarioController(InsightsUsuarioService insightsService, UserService userService) {
        this.insightsService = insightsService;
        this.userService = userService;
    }

    private User usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.buscarPorEmail(email);
    }

    @GetMapping
    public InsightsUsuarioResponse insights() {
        return insightsService.gerarInsights(usuarioLogado());
    }
}