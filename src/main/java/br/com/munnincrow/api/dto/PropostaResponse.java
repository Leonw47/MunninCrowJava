package br.com.munnincrow.api.dto;

import java.time.LocalDateTime;

public class PropostaResponse {

    public Long id;
    public String titulo;
    public String conteudo;
    public Long editalId;
    public Long criadoPorId;
    public LocalDateTime dataCriacao;
}