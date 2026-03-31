package br.com.munnincrow.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao_tutoria")
public class AvaliacaoTutoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_id", nullable = false, unique = true)
    private SolicitacaoTutoria solicitacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultor_id", nullable = false)
    private User consultor;

    @Column(nullable = false)
    private int nota; // 1 a 5

    @Column(length = 2000)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // getters e setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public SolicitacaoTutoria getSolicitacao() {return solicitacao;}
    public void setSolicitacao(SolicitacaoTutoria solicitacao) {this.solicitacao = solicitacao;}
    public User getConsultor() {return consultor;}
    public void setConsultor(User consultor) {this.consultor = consultor;}
    public int getNota() {return nota;}
    public void setNota(int nota) {this.nota = nota;}
    public String getComentario() {return comentario;}
    public void setComentario(String comentario) {this.comentario = comentario;}
    public LocalDateTime getDataCriacao() {return dataCriacao;}
    public void setDataCriacao(LocalDateTime dataCriacao) {this.dataCriacao = dataCriacao;}
}