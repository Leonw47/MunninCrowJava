package br.com.munnincrow.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AcompanhamentoResponse {
    public Long propostaId;
    public String statusAtual;
    public LocalDateTime ultimaAtualizacao;
    public List<EventoAcompanhamentoResponse> eventos;
}