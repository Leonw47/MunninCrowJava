package br.com.munnincrow.api.dto;

public class EstatisticaCategoriaResponse {
    public String categoria;
    public Long quantidade;

    public EstatisticaCategoriaResponse(String categoria, Long quantidade) {
        this.categoria = categoria;
        this.quantidade = quantidade;
    }
}