package br.com.munnincrow.api.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FormularioEdital {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Edital edital;

    @OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampoFormulario> campos = new ArrayList<>();

    public Long getId() {return id;}
    public void setId(Long id) {
        this.id = id;
    }
    public Edital getEdital() {
        return edital;
    }
    public void setEdital(Edital edital) {
        this.edital = edital;
    }
    public List<CampoFormulario> getCampos() {
        return campos;
    }
    public void setCampos(List<CampoFormulario> campos) {
        this.campos = campos;
    }
}