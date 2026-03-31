package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.enums.OrgaoEdital;
import br.com.munnincrow.api.model.enums.StatusEdital;

import java.time.LocalDate;

public class EditalResponse {

    public Long id;
    public String titulo;
    public String descricao; // vem de descricaoCurta
    public OrgaoEdital orgao;
    public String estado;
    public String areaTematica;
    public String categoria;
    public LocalDate dataAbertura;
    public LocalDate dataEncerramento;
    public String link; // vem de linkOficial
    public StatusEdital status;
    public LocalDate dataImportacao;
}