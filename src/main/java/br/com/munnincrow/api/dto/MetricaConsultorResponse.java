package br.com.munnincrow.api.dto;

public class MetricaConsultorResponse {
    public Long consultorId;
    public double media;
    public int totalAvaliacoes;
    public int nota5;
    public int nota4;
    public int nota3;
    public int nota2;
    public int nota1;
    public double percentualPositivas; // <-- CAMPO QUE FALTAVA
}