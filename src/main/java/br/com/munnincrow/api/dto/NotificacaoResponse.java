package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.Notificacao;

import java.time.LocalDateTime;

public class NotificacaoResponse {
    public Long id;
    public String mensagem;
    public boolean lida;
    public LocalDateTime dataCriacao;
    public Long editalId;
    public String editalTitulo;

    public static NotificacaoResponse from(Notificacao n) {
        NotificacaoResponse r = new NotificacaoResponse();
        r.id = n.getId();
        r.mensagem = n.getMensagem();
        r.lida = n.isLida();
        r.dataCriacao = n.getDataCriacao();
        r.editalId = n.getEdital().getId();
        r.editalTitulo = n.getEdital().getTitulo();
        return r;
    }
}