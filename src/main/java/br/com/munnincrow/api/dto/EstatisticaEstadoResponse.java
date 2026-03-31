package br.com.munnincrow.api.dto;

public class EstatisticaEstadoResponse {
    public String estado;
    public Long quantidade;

    public EstatisticaEstadoResponse(String estado, Long quantidade) {
        this.estado = estado;
        this.quantidade = quantidade;
    }
}