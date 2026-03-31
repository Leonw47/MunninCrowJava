package br.com.munnincrow.api.dto;

import java.time.LocalDateTime;

public class AvaliacaoTutoriaResponse {
    public Long id;
    public Long solicitacaoId;
    public Long consultorId;
    public int nota;
    public String comentario;
    public LocalDateTime dataCriacao;
}