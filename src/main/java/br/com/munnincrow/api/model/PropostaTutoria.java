package br.com.munnincrow.api.model;

import br.com.munnincrow.api.model.enums.StatusPropostaTutoria;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "proposta_tutoria")
public class PropostaTutoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPropostaTutoria status = StatusPropostaTutoria.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private User autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private SolicitacaoTutoria solicitacao;

    // getters e setters
    public SolicitacaoTutoria getSolicitacao() {return solicitacao;}
    public void setSolicitacao(SolicitacaoTutoria solicitacao) {this.solicitacao = solicitacao;}
    public User getAutor() {return autor;}
    public void setAutor(User autor) {this.autor = autor;}
    public StatusPropostaTutoria getStatus() {return status;}
    public void setStatus(StatusPropostaTutoria status) {this.status = status;}
    public LocalDateTime getDataCriacao() {return dataCriacao;}
    public void setDataCriacao(LocalDateTime dataCriacao) {this.dataCriacao = dataCriacao;}
    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
    public Double getValor() {return valor;}
    public void setValor(Double valor) {this.valor = valor;}
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
}