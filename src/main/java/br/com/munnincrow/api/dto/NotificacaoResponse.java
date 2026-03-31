package br.com.munnincrow.api.dto;

import java.time.LocalDateTime;

public class NotificacaoResponse {

    public Long id;
    public String mensagem;
    public LocalDateTime dataCriacao;
    public boolean lida;

}