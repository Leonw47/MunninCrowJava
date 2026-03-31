package br.com.munnincrow.api.dto;

import java.util.List;

public class NegociacaoTutoriaResponse {
    public SolicitacaoTutoriaResponse solicitacao;
    public List<PropostaTutoriaResponse> propostas;
    public PermissoesNegociacao permissoes;
}