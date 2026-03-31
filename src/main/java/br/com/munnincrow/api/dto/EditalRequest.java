package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.enums.OrgaoEdital;
import br.com.munnincrow.api.model.enums.StatusEdital;

import java.time.LocalDate;

public class EditalRequest {

    public String titulo;
    public String descricao; // mapeado para descricaoCurta
    public OrgaoEdital orgao;
    public String estado; // ES, RJ, SP, MG
    public String areaTematica;
    public String categoria;
    public LocalDate dataAbertura;
    public LocalDate dataEncerramento;
    public String link; // mapeado para linkOficial
    public StatusEdital status;
}