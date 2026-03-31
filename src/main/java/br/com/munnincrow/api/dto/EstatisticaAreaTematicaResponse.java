package br.com.munnincrow.api.dto;

public class EstatisticaAreaTematicaResponse {
    public String areaTematica;
    public Long quantidade;

    public EstatisticaAreaTematicaResponse(String areaTematica, Long quantidade) {
        this.areaTematica = areaTematica;
        this.quantidade = quantidade;
    }
}