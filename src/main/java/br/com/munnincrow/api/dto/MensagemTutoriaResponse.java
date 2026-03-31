package br.com.munnincrow.api.dto;

import java.time.LocalDateTime;

public class MensagemTutoriaResponse {
    public Long id;
    public Long autorId;
    public Long solicitacaoId;
    public String conteudo;
    public LocalDateTime dataCriacao;
    public boolean lida;
}