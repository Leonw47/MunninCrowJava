package br.com.munnincrow.api.model;

import jakarta.persistence.*;

@Entity
public class CampoFormulario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private FormularioEdital formulario;

    private String nomeCampo;
    private String tipo; // texto, número, upload, seleção etc.
    private boolean obrigatorio;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public FormularioEdital getFormulario() {
        return formulario;
    }
    public void setFormulario(FormularioEdital formulario) {
        this.formulario = formulario;
    }
    public String getNomeCampo() {
        return nomeCampo;
    }
    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public boolean isObrigatorio() {return obrigatorio;}
    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }
}
