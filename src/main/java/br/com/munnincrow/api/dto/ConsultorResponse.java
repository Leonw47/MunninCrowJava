package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.User;

public class ConsultorResponse {

    public Long id;
    public String nome;
    public String email;
    public double mediaAvaliacoes;
    public int totalAvaliacoes;

    public static ConsultorResponse from(User u) {
        ConsultorResponse resp = new ConsultorResponse();
        resp.id = u.getId();
        resp.nome = u.getNome();
        resp.email = u.getEmail();
        resp.mediaAvaliacoes = u.getMediaAvaliacoes();
        resp.totalAvaliacoes = u.getTotalAvaliacoes();
        return resp;
    }
}