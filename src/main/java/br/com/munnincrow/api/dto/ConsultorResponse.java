package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.User;

public class ConsultorResponse {
    public Long id;
    public String nome;
    public Double mediaAvaliacoes;
    public Integer totalAvaliacoes;

    public static ConsultorResponse from(User u) {
        ConsultorResponse r = new ConsultorResponse();
        r.id = u.getId();
        r.nome = u.getNome();
        r.mediaAvaliacoes = u.getMediaAvaliacoes();
        r.totalAvaliacoes = u.getTotalAvaliacoes();
        return r;
    }
}
