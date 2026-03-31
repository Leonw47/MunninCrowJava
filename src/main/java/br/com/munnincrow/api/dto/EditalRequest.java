package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.enums.OrgaoEdital;
import br.com.munnincrow.api.model.enums.StatusEdital;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EditalRequest {

    @NotBlank
    public String titulo;

    public String descricao;

    @NotNull
    public OrgaoEdital orgao;

    @NotBlank
    public String estado;

    public String categoria;

    public String areaTematica;

    public String areaTematicaReal;

    @NotNull
    public LocalDate dataAbertura;

    @NotNull
    public LocalDate dataEncerramento;

    @NotBlank
    public String link;

    public Double valorMaximo;

    public String objetivo;

    public String publicoAlvo;

    public StatusEdital status;
}