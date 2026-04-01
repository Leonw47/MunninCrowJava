package br.com.munnincrow.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class EventoAcompanhamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private AcompanhamentoProposta acompanhamento;

    private LocalDateTime data;

    private String tipo; // status_alterado, campo_editado, lembrete, alerta_prazo

    private String descricao;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public AcompanhamentoProposta getAcompanhamento() {
        return acompanhamento;
    }
    public void setAcompanhamento(AcompanhamentoProposta acompanhamento) {
        this.acompanhamento = acompanhamento;
    }
    public LocalDateTime getData() {
        return data;
    }
    public void setData(LocalDateTime data) {
        this.data = data;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}