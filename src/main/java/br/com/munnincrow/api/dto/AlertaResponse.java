package br.com.munnincrow.api.dto;

import java.time.LocalDateTime;

public class AlertaResponse {
    public Long id;
    public String tipo;
    public String mensagem;
    public LocalDateTime dataGeracao;
    public boolean lido;
}