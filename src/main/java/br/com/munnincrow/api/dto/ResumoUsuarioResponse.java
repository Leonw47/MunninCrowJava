package br.com.munnincrow.api.dto;

import java.util.List;

public class ResumoUsuarioResponse {

    public PerfilResponse perfil;

    public List<EditalResponse> recomendacoes;

    public List<NotificacaoResponse> notificacoes;

    public List<HistoricoInteracaoResponse> historicoRecente;
}