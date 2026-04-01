package br.com.munnincrow.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AcompanhamentoProposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Proposta proposta;

    private String statusAtual; // rascunho, enviada, em_analise, aprovada, rejeitada

    private LocalDateTime ultimaAtualizacao;

    @OneToMany(mappedBy = "acompanhamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventoAcompanhamento> eventos = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Proposta getProposta() {
        return proposta;
    }
    public void setProposta(Proposta proposta) {
        this.proposta = proposta;
    }
    public String getStatusAtual() {
        return statusAtual;
    }
    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }
    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }
    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
    public List<EventoAcompanhamento> getEventos() {
        return eventos;
    }
    public void setEventos(List<EventoAcompanhamento> eventos) {
        this.eventos = eventos;
    }
}