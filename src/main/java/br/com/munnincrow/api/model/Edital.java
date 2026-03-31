package br.com.munnincrow.api.model;

import br.com.munnincrow.api.model.enums.FonteImportacao;
import br.com.munnincrow.api.model.enums.OrgaoEdital;
import br.com.munnincrow.api.model.enums.StatusEdital;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "edital")
public class Edital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 2000)
    private String descricaoCurta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrgaoEdital orgao;

    @Column(nullable = false, length = 2)
    private String estado; // ES, RJ, SP, MG

    @Column(nullable = false)
    private String linkOficial;

    @Column(nullable = false)
    private LocalDate dataAbertura;

    @Column(nullable = false)
    private LocalDate dataEncerramento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEdital status = StatusEdital.ABERTO;

    @Column
    private String areaTematica;

    @Column
    private String categoria;

    @Column(nullable = false)
    private LocalDate dataImportacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FonteImportacao fonte;

    @PrePersist
    public void prePersist() {
        this.dataImportacao = LocalDate.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricaoCurta() { return descricaoCurta; }
    public void setDescricaoCurta(String descricaoCurta) { this.descricaoCurta = descricaoCurta; }

    public OrgaoEdital getOrgao() { return orgao; }
    public void setOrgao(OrgaoEdital orgao) { this.orgao = orgao; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getLinkOficial() { return linkOficial; }
    public void setLinkOficial(String linkOficial) { this.linkOficial = linkOficial; }

    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDate getDataEncerramento() { return dataEncerramento; }
    public void setDataEncerramento(LocalDate dataEncerramento) { this.dataEncerramento = dataEncerramento; }

    public StatusEdital getStatus() { return status; }
    public void setStatus(StatusEdital status) { this.status = status; }

    public String getAreaTematica() { return areaTematica; }
    public void setAreaTematica(String areaTematica) { this.areaTematica = areaTematica; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDate getDataImportacao() { return dataImportacao; }

    public FonteImportacao getFonte() { return fonte; }
    public void setFonte(FonteImportacao fonte) { this.fonte = fonte; }
}