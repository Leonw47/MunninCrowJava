package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.EditalResponse;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.service.RecomendacaoEditalService;
import br.com.munnincrow.api.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendacoes")
public class RecomendacaoController {

    private final RecomendacaoEditalService recomendacaoService;
    private final UserService userService;

    public RecomendacaoController(RecomendacaoEditalService recomendacaoService, UserService userService) {
        this.recomendacaoService = recomendacaoService;
        this.userService = userService;
    }

    private User usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.buscarPorEmail(email);
    }

    @GetMapping
    public List<EditalResponse> recomendar() {
        User usuario = usuarioLogado();

        return recomendacaoService.recomendarPara(usuario)
                .stream()
                .map(e -> {
                    EditalResponse r = new EditalResponse();
                    r.id = e.getId();
                    r.titulo = e.getTitulo();
                    r.descricaoCurta = e.getDescricaoCurta();
                    r.orgao = e.getOrgao();
                    r.estado = e.getEstado();
                    r.areaTematica = e.getAreaTematica();
                    r.areaTematicaReal = e.getAreaTematicaReal();
                    r.categoria = e.getCategoria();
                    r.dataAbertura = e.getDataAbertura();
                    r.dataEncerramento = e.getDataEncerramento();
                    r.link = e.getLinkOficial();
                    r.valorMaximo = e.getValorMaximo();
                    r.objetivo = e.getObjetivo();
                    r.publicoAlvo = e.getPublicoAlvo();
                    r.status = e.getStatus();
                    r.dataImportacao = e.getDataImportacao();
                    return r;
                })
                .toList();
    }
}