package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.service.EditalService;
import br.com.munnincrow.api.service.InteracaoEditalService;
import br.com.munnincrow.api.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interacoes")
public class InteracaoEditalController {

    private final InteracaoEditalService interacaoService;
    private final UserService userService;
    private final EditalService editalService;

    public InteracaoEditalController(
            InteracaoEditalService interacaoService,
            UserService userService,
            EditalService editalService
    ) {
        this.interacaoService = interacaoService;
        this.userService = userService;
        this.editalService = editalService;
    }

    private User usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.buscarPorEmail(email);
    }

    @PostMapping("/{editalId}/clicar")
    public void clicar(@PathVariable Long editalId) {
        User usuario = usuarioLogado();
        Edital edital = editalService.buscarPorId(editalId);
        interacaoService.registrarClique(usuario, edital);
    }

    @PostMapping("/{editalId}/favoritar")
    public void favoritar(@PathVariable Long editalId) {
        User usuario = usuarioLogado();
        Edital edital = editalService.buscarPorId(editalId);
        interacaoService.registrarFavorito(usuario, edital);
    }

    @PostMapping("/{editalId}/inscrever")
    public void inscrever(@PathVariable Long editalId) {
        User usuario = usuarioLogado();
        Edital edital = editalService.buscarPorId(editalId);
        interacaoService.registrarInscricao(usuario, edital);
    }
}