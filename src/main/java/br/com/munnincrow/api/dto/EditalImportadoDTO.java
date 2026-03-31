package br.com.munnincrow.api.dto;

import java.time.LocalDate;

public class EditalImportadoDTO {
    public String titulo;
    public String link;
    public String orgao;
    public String estado;
    public String categoria;
    public String areaTematica;
    public LocalDate dataAbertura;
    public LocalDate dataFechamento;

    // opcionais, caso importadores forneçam
    public String status;
    public String descricao;
}