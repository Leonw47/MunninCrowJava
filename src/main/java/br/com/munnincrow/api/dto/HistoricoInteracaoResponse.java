package br.com.munnincrow.api.dto;

import java.time.LocalDate;

public class HistoricoInteracaoResponse {
    public Long editalId;
    public String titulo;
    public boolean clicou;
    public boolean favoritou;
    public boolean seInscreveu;
    public LocalDate data;
}