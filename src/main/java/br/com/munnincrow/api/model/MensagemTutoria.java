package br.com.munnincrow.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensagem_tutoria")
public class MensagemTutoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private SolicitacaoTutoria solicitacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private User autor;

    @Column(nullable = false, length = 2000)
    private String conteudo;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(nullable = false)
    private boolean lida = false;

    // getters e setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public SolicitacaoTutoria getSolicitacao() {return solicitacao;}
    public void setSolicitacao(SolicitacaoTutoria solicitacao) {this.solicitacao = solicitacao;}
    public User getAutor() {return autor;}
    public void setAutor(User autor) {this.autor = autor;}
    public String getConteudo() {return conteudo;}
    public void setConteudo(String conteudo) {this.conteudo = conteudo;}
    public LocalDateTime getDataCriacao() {return dataCriacao;}
    public void setDataCriacao(LocalDateTime dataCriacao) {this.dataCriacao = dataCriacao;}
    public boolean isLida() {return lida;}
    public void setLida(boolean lida) {this.lida = lida;}
}