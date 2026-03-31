package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.InteracaoEdital;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.EditalRepository;
import br.com.munnincrow.api.repository.InteracaoEditalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class RecomendacaoEditalService {

    private final EditalRepository editalRepo;
    private final InteracaoEditalRepository interacaoRepo;

    public RecomendacaoEditalService(EditalRepository editalRepo, InteracaoEditalRepository interacaoRepo) {
        this.editalRepo = editalRepo;
        this.interacaoRepo = interacaoRepo;
    }

    public List<Edital> recomendarPara(User usuario) {

        List<Edital> editais = editalRepo.findAll();

        return editais.stream()
                .map(e -> new EditalScore(e, calcularScore(e, usuario)))
                .sorted(Comparator.comparingDouble(EditalScore::score).reversed())
                .limit(10)
                .map(EditalScore::edital)
                .toList();
    }

    public double calcularScore(Edital edital, User usuario) {
        double aderencia = calcularAderenciaTematica(edital, usuario);
        double maturidade = calcularCompatibilidadeMaturidade(edital, usuario);
        double valor = calcularValorFinanceiro(edital, usuario);
        double historico = calcularHistorico(edital, usuario);
        double novidade = calcularNovidade(edital);
        double urgencia = calcularUrgencia(edital);

        return (0.35 * aderencia) +
                (0.20 * maturidade) +
                (0.15 * valor) +
                (0.15 * historico) +
                (0.10 * novidade) +
                (0.05 * urgencia);
    }

    public double calcularAderenciaTematica(Edital edital, User usuario) {
        if (usuario.getSegmento() == null || edital.getAreaTematicaReal() == null) return 0.3;

        String area = edital.getAreaTematicaReal().toLowerCase();
        String seg = usuario.getSegmento().toLowerCase();

        return area.contains(seg) ? 1.0 : 0.2;
    }

    public double calcularCompatibilidadeMaturidade(Edital edital, User usuario) {
        if (usuario.getMaturidade() == null) return 0.3;

        String objetivo = edital.getObjetivo() != null ? edital.getObjetivo().toLowerCase() : "";

        if (usuario.getMaturidade().equalsIgnoreCase("ideia") && objetivo.contains("ideação")) return 1.0;
        if (usuario.getMaturidade().equalsIgnoreCase("tração") && objetivo.contains("escala")) return 1.0;

        return 0.4;
    }

    public double calcularValorFinanceiro(Edital edital, User usuario) {
        if (edital.getValorMaximo() == null || usuario.getFaturamentoAnual() == null) return 0.5;

        double v = edital.getValorMaximo();
        double f = usuario.getFaturamentoAnual();

        if (v >= f * 0.5 && v <= f * 5) return 1.0;
        return 0.3;
    }

    public double calcularHistorico(Edital edital, User usuario) {
        List<InteracaoEdital> interacoes =
                interacaoRepo.findByUsuarioAndEdital(usuario, edital);

        if (interacoes.isEmpty()) return 0.2;

        double score = 0;

        for (InteracaoEdital i : interacoes) {
            if (i.isClicou()) score += 0.2;
            if (i.isFavoritou()) score += 0.5;
            if (i.isSeInscreveu()) score += 1.0;
        }

        return Math.min(score, 1.0);
    }

    public double calcularNovidade(Edital edital) {
        if (edital.getDataImportacao() == null) return 0.3;

        long dias = java.time.temporal.ChronoUnit.DAYS.between(edital.getDataImportacao(), LocalDate.now());

        if (dias <= 3) return 1.0;
        if (dias <= 10) return 0.7;
        return 0.3;
    }

    public double calcularUrgencia(Edital edital) {
        if (edital.getDataEncerramento() == null) return 0.3;

        long dias = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), edital.getDataEncerramento());

        if (dias <= 3) return 1.0;
        if (dias <= 10) return 0.7;
        return 0.3;
    }

    private record EditalScore(Edital edital, double score) {}
}