package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.enums.OrgaoEdital;
import br.com.munnincrow.api.model.enums.StatusEdital;

import java.time.LocalDate;

public class EditalResponse {

    public Long id;
    public String titulo;
    public String descricao;
    public OrgaoEdital orgao;
    public String estado;
    public String categoria;
    public String areaTematica;
    public String areaTematicaReal;
    public LocalDate dataAbertura;
    public LocalDate dataEncerramento;
    public String link;
    public Double valorMaximo;
    public String objetivo;
    public String publicoAlvo;
    public StatusEdital status;
    public LocalDate dataImportacao;
}