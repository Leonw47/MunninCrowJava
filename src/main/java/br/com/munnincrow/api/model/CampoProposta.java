package br.com.munnincrow.api.model;

import jakarta.persistence.*;

@Entity
public class CampoProposta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Proposta proposta;

    private String nomeCampo;     // "Resumo", "Justificativa", "Objetivos"
    private String valor;         // texto preenchido pelo usuário
    private boolean concluido;    // protege contra sobrescrita automática (RNF07)

    public Long getId() {return id;}
    public void setId(Long id) {
        this.id = id;
    }
    public Proposta getProposta() {
        return proposta;
    }
    public void setProposta(Proposta proposta) {
        this.proposta = proposta;
    }
    public String getNomeCampo() {
        return nomeCampo;
    }
    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }
    public String getValor() {
        return valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
    public boolean isConcluido() {
        return concluido;
    }
    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }
}
