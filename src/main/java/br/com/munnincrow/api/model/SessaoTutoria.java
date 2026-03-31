package br.com.munnincrow.api.model;

import br.com.munnincrow.api.model.enums.StatusSessao;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessao_tutoria")
public class SessaoTutoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private SolicitacaoTutoria solicitacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultor_id", nullable = false)
    private User consultor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empreendedor_id", nullable = false)
    private User empreendedor;

    @Column(nullable = false)
    private LocalDateTime inicio;

    @Column(nullable = false)
    private LocalDateTime fim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSessao status = StatusSessao.AGENDADA;

    private String observacoes;

    // getters e setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public SolicitacaoTutoria getSolicitacao() {return solicitacao;}
    public void setSolicitacao(SolicitacaoTutoria solicitacao) {this.solicitacao = solicitacao;}
    public User getConsultor() {return consultor;}
    public void setConsultor(User consultor) {this.consultor = consultor;}
    public User getEmpreendedor() {return empreendedor;}
    public void setEmpreendedor(User empreendedor) {this.empreendedor = empreendedor;}
    public LocalDateTime getInicio() {return inicio;}
    public void setInicio(LocalDateTime inicio) {this.inicio = inicio;}
    public LocalDateTime getFim() {return fim;}
    public void setFim(LocalDateTime fim) {this.fim = fim;}
    public StatusSessao getStatus() {return status;}
    public void setStatus(StatusSessao status) {this.status = status;}
    public String getObservacoes() {return observacoes;}
    public void setObservacoes(String observacoes) {this.observacoes = observacoes;}
}