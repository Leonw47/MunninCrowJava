package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.service.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final SolicitacaoTutoriaService solicitacaoService;
    private final PropostaTutoriaService propostaService;
    private final SessaoTutoriaService sessaoService;
    private final PagamentoTutoriaService pagamentoService;
    private final AvaliacaoTutoriaService avaliacaoService;
    private final HistoricoTutoriaService historicoService;
    private final MetricaConsultorService metricaService;
    private final RankingConsultorService rankingService;

    public DashboardService(
            SolicitacaoTutoriaService solicitacaoService,
            PropostaTutoriaService propostaService,
            SessaoTutoriaService sessaoService,
            PagamentoTutoriaService pagamentoService,
            AvaliacaoTutoriaService avaliacaoService,
            HistoricoTutoriaService historicoService,
            MetricaConsultorService metricaService,
            RankingConsultorService rankingService
    ) {
        this.solicitacaoService = solicitacaoService;
        this.propostaService = propostaService;
        this.sessaoService = sessaoService;
        this.pagamentoService = pagamentoService;
        this.avaliacaoService = avaliacaoService;
        this.historicoService = historicoService;
        this.metricaService = metricaService;
        this.rankingService = rankingService;
    }

    public EmpreendedorDashboardResponse montarDashboardEmpreendedor(Long userId) {

        EmpreendedorDashboardResponse resp = new EmpreendedorDashboardResponse();

        resp.solicitacoes = solicitacaoService.listarPorEmpreendedor(userId);
        resp.propostasRecebidas = propostaService.listarPropostasRecebidas(userId);
        resp.proximasSessoes = sessaoService.listarProximasPorEmpreendedor(userId);
        resp.pagamentos = pagamentoService.listarPorEmpreendedor(userId);
        resp.historico = historicoService.listarPorUsuario(userId);
        resp.avaliacoesFeitas = avaliacaoService.listarPorEmpreendedor(userId);

        return resp;
    }

    public ConsultorDashboardResponse montarDashboardConsultor(Long userId) {

        ConsultorDashboardResponse resp = new ConsultorDashboardResponse();

        resp.solicitacoesDisponiveis = solicitacaoService.listarAbertasParaConsultor(userId);
        resp.propostasEnviadas = propostaService.listarPorAutor(userId);
        resp.proximasSessoes = sessaoService.listarProximasPorConsultor(userId);
        resp.avaliacoesRecebidas = avaliacaoService.listarPorConsultor(userId);
        resp.metricas = metricaService.buscarMetricas(userId);
        resp.ranking = rankingService.buscarRanking(userId);

        return resp;
    }
}