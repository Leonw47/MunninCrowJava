package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.*;

import java.util.List;

public class EmpreendedorDashboardResponse {
    public List<SolicitacaoTutoria> solicitacoes;
    public List<PropostaTutoria> propostasRecebidas;
    public List<SessaoTutoria> proximasSessoes;
    public List<PagamentoTutoria> pagamentos;
    public List<HistoricoTutoriaResponse> historico;
    public List<AvaliacaoTutoria> avaliacoesFeitas;
}