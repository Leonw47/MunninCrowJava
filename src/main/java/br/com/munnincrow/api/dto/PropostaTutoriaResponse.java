package br.com.munnincrow.api.dto;

import java.time.LocalDateTime;

public class PropostaTutoriaResponse {
    public Long id;
    public Double valor;
    public String descricao;
    public LocalDateTime dataCriacao;
    public String status;
    public Long autorId;
    public Long solicitacaoId;
}
