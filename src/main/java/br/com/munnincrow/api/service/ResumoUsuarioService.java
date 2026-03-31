package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.*;
import br.com.munnincrow.api.model.Notificacao;
import br.com.munnincrow.api.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumoUsuarioService {

    private final RecomendacaoEditalService recomendacaoService;
    private final NotificacaoService notificacaoService;
    private final HistoricoInteracaoService historicoService;

    public ResumoUsuarioService(
            RecomendacaoEditalService recomendacaoService,
            NotificacaoService notificacaoService,
            HistoricoInteracaoService historicoService
    ) {
        this.recomendacaoService = recomendacaoService;
        this.notificacaoService = notificacaoService;
        this.historicoService = historicoService;
    }

    @Cacheable(value = "resumoUsuario", key = "#usuario.id")
    public ResumoUsuarioResponse gerarResumo(User usuario) {

        ResumoUsuarioResponse resp = new ResumoUsuarioResponse();

        // Perfil
        PerfilResponse p = new PerfilResponse();
        p.id = usuario.getId();
        p.nome = usuario.getNome();
        p.email = usuario.getEmail();
        p.segmento = usuario.getSegmento();
        p.maturidade = usuario.getMaturidade();
        p.faturamentoAnual = usuario.getFaturamentoAnual();
        resp.perfil = p;

        // Recomendações (já otimizadas internamente)
        resp.recomendacoes = recomendacaoService.recomendarPara(usuario)
                .stream()
                .map(EditalResponse::from)
                .toList();

        // Notificações
        resp.notificacoes = notificacaoService.listar(usuario)
                .stream()
                .map(NotificacaoResponse::from)
                .toList();

        // Histórico recente (últimos 10)
        resp.historicoRecente = historicoService.listarHistorico(usuario)
                .stream()
                .limit(10)
                .toList();

        return resp;
    }
}