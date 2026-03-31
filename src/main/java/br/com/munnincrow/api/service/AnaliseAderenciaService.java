package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.AnaliseAderenciaResponse;
import br.com.munnincrow.api.dto.SugestaoCampoResponse;
import br.com.munnincrow.api.model.CampoProposta;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.Proposta;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.CampoPropostaRepository;
import br.com.munnincrow.api.repository.PropostaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AnaliseAderenciaService {

    private final PropostaRepository propostaRepo;
    private final CampoPropostaRepository campoRepo;
    private final RecomendacaoEditalService recomendacaoService;

    public AnaliseAderenciaService(PropostaRepository propostaRepo,
                                   CampoPropostaRepository campoRepo,
                                   RecomendacaoEditalService recomendacaoService) {
        this.propostaRepo = propostaRepo;
        this.campoRepo = campoRepo;
        this.recomendacaoService = recomendacaoService;
    }

    public AnaliseAderenciaResponse analisar(Long propostaId) {

        Proposta proposta = propostaRepo.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));

        Edital edital = proposta.getEdital();
        User usuario = proposta.getUsuario();

        double aderencia = recomendacaoService.calcularAderenciaTematica(edital, usuario);
        double maturidade = recomendacaoService.calcularCompatibilidadeMaturidade(edital, usuario);
        double valor = recomendacaoService.calcularValorFinanceiro(edital, usuario);
        double historico = recomendacaoService.calcularHistorico(edital, usuario);
        double novidade = recomendacaoService.calcularNovidade(edital);
        double urgencia = recomendacaoService.calcularUrgencia(edital);

        double scoreGeral = (aderencia * 0.35 +
                maturidade * 0.20 +
                valor * 0.15 +
                historico * 0.15 +
                novidade * 0.10 +
                urgencia * 0.05) * 100;

        AnaliseAderenciaResponse resp = new AnaliseAderenciaResponse();
        resp.scoreGeral = scoreGeral;

        resp.criterios = Map.of(
                "aderencia", aderencia,
                "maturidade", maturidade,
                "valor", valor,
                "historico", historico,
                "novidade", novidade,
                "urgencia", urgencia
        );

        resp.sugestoes = proposta.getCampos().stream()
                .map(c -> {
                    SugestaoCampoResponse s = new SugestaoCampoResponse();
                    s.nomeCampo = c.getNomeCampo();
                    s.sugestao = gerarSugestaoParaCampo(c, edital);
                    return s;
                })
                .toList();

        return resp;
    }

    private String gerarSugestaoParaCampo(CampoProposta campo, Edital edital) {
        return """
                Revise o campo "%s" para reforçar a conexão com o objetivo do edital "%s".
                Inclua dados concretos, impacto mensurável e alinhamento com a área temática "%s".
                """.formatted(
                campo.getNomeCampo(),
                edital.getTitulo(),
                edital.getAreaTematicaReal()
        );
    }
}