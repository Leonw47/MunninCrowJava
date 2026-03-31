package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.enums.OrgaoEdital;
import br.com.munnincrow.api.model.enums.StatusEdital;

import java.time.LocalDate;

public class EditalResponse {

    public Long id;
    public String titulo;
    public String descricaoCurta;
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

    public static EditalResponse from(Edital e) {
        EditalResponse r = new EditalResponse();
        r.id = e.getId();
        r.titulo = e.getTitulo();
        r.descricaoCurta = e.getDescricaoCurta();
        r.orgao = e.getOrgao();
        r.estado = e.getEstado();
        r.areaTematica = e.getAreaTematica();
        r.areaTematicaReal = e.getAreaTematicaReal();
        r.categoria = e.getCategoria();
        r.dataAbertura = e.getDataAbertura();
        r.dataEncerramento = e.getDataEncerramento();
        r.link = e.getLinkOficial();
        r.valorMaximo = e.getValorMaximo();
        r.objetivo = e.getObjetivo();
        r.publicoAlvo = e.getPublicoAlvo();
        r.status = e.getStatus();
        r.dataImportacao = e.getDataImportacao();
        return r;
    }
}