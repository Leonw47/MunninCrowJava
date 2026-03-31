package br.com.munnincrow.api.dto;

import java.time.LocalDate;

public class GastoProjetoResponse {

    public Long id;
    public String categoria;
    public Double valor;
    public LocalDate data;
    public String descricao;
    public Long acompanhamentoId;
}