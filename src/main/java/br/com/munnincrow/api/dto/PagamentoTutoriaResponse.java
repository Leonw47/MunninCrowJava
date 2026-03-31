package br.com.munnincrow.api.dto;

import java.time.LocalDateTime;

public class PagamentoTutoriaResponse {
    public Long id;
    public Long propostaId;
    public Long empreendedorId;
    public Long consultorId;
    public double valor;
    public String status;
    public LocalDateTime dataCriacao;
    public LocalDateTime dataConfirmacao;
}
