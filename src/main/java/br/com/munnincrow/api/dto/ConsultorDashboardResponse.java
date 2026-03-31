package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.AvaliacaoTutoria;
import br.com.munnincrow.api.model.PropostaTutoria;
import br.com.munnincrow.api.model.SessaoTutoria;
import br.com.munnincrow.api.model.SolicitacaoTutoria;

import java.util.List;

public class ConsultorDashboardResponse {
    public List<SolicitacaoTutoria> solicitacoesDisponiveis;
    public List<PropostaTutoria> propostasEnviadas;
    public List<SessaoTutoria> proximasSessoes;
    public List<AvaliacaoTutoria> avaliacoesRecebidas;
    public MetricaConsultorResponse metricas;
    public RankingConsultorResponse ranking;
}
