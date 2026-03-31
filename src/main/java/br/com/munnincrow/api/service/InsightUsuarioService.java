package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.*;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.InteracaoEdital;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.EditalRepository;
import br.com.munnincrow.api.repository.InteracaoEditalRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InsightUsuarioService {

    private final InteracaoEditalRepository interacaoRepo;
    private final EditalRepository editalRepo;
    private final RecomendacaoEditalService recomendacaoService;

    public InsightUsuarioService(
            InteracaoEditalRepository interacaoRepo,
            EditalRepository editalRepo,
            RecomendacaoEditalService recomendacaoService
    ) {
        this.interacaoRepo = interacaoRepo;
        this.editalRepo = editalRepo;
        this.recomendacaoService = recomendacaoService;
    }

    public InsightsUsuarioResponse gerarInsights(User usuario) {

        InsightsUsuarioResponse resp = new InsightsUsuarioResponse();

        List<InteracaoEdital> interacoes = interacaoRepo.findByUsuario(usuario);

        // ---------------------------
        // TENDÊNCIAS DO USUÁRIO
        // ---------------------------

        resp.categoriasMaisAcessadas = interacoes.stream()
                .map(i -> i.getEdital().getCategoria())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        resp.areasTematicasMaisAcessadas = interacoes.stream()
                .map(i -> i.getEdital().getAreaTematicaReal())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // ORGAO É ENUM → converter para string
        resp.orgaosMaisAcessados = interacoes.stream()
                .map(i -> i.getEdital().getOrgao())
                .filter(Objects::nonNull)
                .map(Enum::name) // Angular-friendly
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // ---------------------------
        // ENGAJAMENTO
        // ---------------------------

        resp.totalCliques = interacoes.stream().filter(InteracaoEdital::isClicou).count();
        resp.totalFavoritos = interacoes.stream().filter(InteracaoEdital::isFavoritou).count();
        resp.totalInscricoes = interacoes.stream().filter(InteracaoEdital::isSeInscreveu).count();

        // ---------------------------
        // MOTIVOS DAS RECOMENDAÇÕES
        // ---------------------------

        resp.motivosRecomendacao = recomendacaoService.recomendarPara(usuario)
                .stream()
                .map(e -> {
                    InsightRecomendacao r = new InsightRecomendacao();
                    r.editalId = e.getId();
                    r.titulo = e.getTitulo();

                    r.aderencia = recomendacaoService.calcularAderenciaTematica(e, usuario);
                    r.maturidade = recomendacaoService.calcularCompatibilidadeMaturidade(e, usuario);
                    r.valor = recomendacaoService.calcularValorFinanceiro(e, usuario);
                    r.historico = recomendacaoService.calcularHistorico(e, usuario);
                    r.novidade = recomendacaoService.calcularNovidade(e);
                    r.urgencia = recomendacaoService.calcularUrgencia(e);

                    r.score = recomendacaoService.calcularScore(e, usuario);

                    return r;
                })
                .toList();

        // ---------------------------
        // OPORTUNIDADES NÃO VISTAS
        // ---------------------------

        List<Edital> recomendados = recomendacaoService.recomendarPara(usuario);

        Set<Long> vistos = interacoes.stream()
                .map(i -> i.getEdital().getId())
                .collect(Collectors.toSet());

        resp.oportunidadesNaoVistas = recomendados.stream()
                .filter(e -> !vistos.contains(e.getId()))
                .map(e -> {
                    InsightOportunidade o = new InsightOportunidade();
                    o.editalId = e.getId();
                    o.titulo = e.getTitulo();
                    o.motivo = "Alta aderência ao seu perfil, mas você ainda não visualizou.";
                    return o;
                })
                .toList();

        return resp;
    }
}