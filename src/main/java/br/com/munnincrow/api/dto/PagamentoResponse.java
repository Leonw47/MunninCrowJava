package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.enums.StatusPagamento;
import java.time.LocalDateTime;

public class PagamentoResponse {
    public Long id;
    public Long propostaId;
    public double valor;
    public StatusPagamento status;
    public LocalDateTime dataCriacao;
    public LocalDateTime dataConfirmacao;
}