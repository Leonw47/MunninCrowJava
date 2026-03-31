package br.com.munnincrow.api.model;

import br.com.munnincrow.api.model.enums.FonteImportacao;
import br.com.munnincrow.api.model.enums.OrgaoEdital;
import br.com.munnincrow.api.model.enums.StatusEdital;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "editais")
public class Edital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(length = 2000)
    private String descricaoCurta;

    private String linkOficial;

    @Enumerated(EnumType.STRING)
    private OrgaoEdital orgao;

    private String estado;

    private String categoria;

    private String areaTematica;

    private String areaTematicaReal;

    private LocalDate dataAbertura;

    private LocalDate dataEncerramento;

    private Double valorMaximo;

    @Column(length = 5000)
    private String objetivo;

    @Column(length = 2000)
    private String publicoAlvo;

    @Enumerated(EnumType.STRING)
    private FonteImportacao fonte;

    @Enumerated(EnumType.STRING)
    private StatusEdital status;

    private LocalDate dataImportacao = LocalDate.now();

    public Long getId() { return id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricaoCurta() { return descricaoCurta; }
    public void setDescricaoCurta(String descricaoCurta) { this.descricaoCurta = descricaoCurta; }

    public String getLinkOficial() { return linkOficial; }
    public void setLinkOficial(String linkOficial) { this.linkOficial = linkOficial; }

    public OrgaoEdital getOrgao() { return orgao; }
    public void setOrgao(OrgaoEdital orgao) { this.orgao = orgao; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getAreaTematica() { return areaTematica; }
    public void setAreaTematica(String areaTematica) { this.areaTematica = areaTematica; }

    public String getAreaTematicaReal() { return areaTematicaReal; }
    public void setAreaTematicaReal(String areaTematicaReal) { this.areaTematicaReal = areaTematicaReal; }

    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDate getDataEncerramento() { return dataEncerramento; }
    public void setDataEncerramento(LocalDate dataEncerramento) { this.dataEncerramento = dataEncerramento; }

    public Double getValorMaximo() { return valorMaximo; }
    public void setValorMaximo(Double valorMaximo) { this.valorMaximo = valorMaximo; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public String getPublicoAlvo() { return publicoAlvo; }
    public void setPublicoAlvo(String publicoAlvo) { this.publicoAlvo = publicoAlvo; }

    public FonteImportacao getFonte() { return fonte; }
    public void setFonte(FonteImportacao fonte) { this.fonte = fonte; }

    public StatusEdital getStatus() { return status; }
    public void setStatus(StatusEdital status) { this.status = status; }

    public LocalDate getDataImportacao() { return dataImportacao; }
    public void setDataImportacao(LocalDate dataImportacao) { this.dataImportacao = dataImportacao; }
}