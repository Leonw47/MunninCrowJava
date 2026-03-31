package br.com.munnincrow.api.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "criadoPor", fetch = FetchType.LAZY)
    private List<Proposta> propostas;

    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)
    private List<MensagemChat> mensagens;

    @Column(nullable = false)
    private double mediaAvaliacoes = 0.0;

    @Column(nullable = false)
    private int totalAvaliacoes = 0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email != null ? email.trim().toLowerCase() : null;
    }
    public List<Proposta> getPropostas() { return propostas; }
    public void setPropostas(List<Proposta> propostas) { this.propostas = propostas; }
    public List<MensagemChat> getMensagens() { return mensagens; }
    public void setMensagens(List<MensagemChat> mensagens) { this.mensagens = mensagens; }
    public double getMediaAvaliacoes() {return mediaAvaliacoes;}
    public void setMediaAvaliacoes(double mediaAvaliacoes) {this.mediaAvaliacoes = mediaAvaliacoes;}
    public int getTotalAvaliacoes() {return totalAvaliacoes;}
    public void setTotalAvaliacoes(int totalAvaliacoes) {this.totalAvaliacoes = totalAvaliacoes;}
}
