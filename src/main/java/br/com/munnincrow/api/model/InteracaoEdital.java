package br.com.munnincrow.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "interacoes_edital")
public class InteracaoEdital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User usuario;

    @ManyToOne(optional = false)
    private Edital edital;

    private boolean clicou;
    private boolean favoritou;
    private boolean seInscreveu;

    private LocalDate data = LocalDate.now();

    public Long getId() { return id; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public Edital getEdital() { return edital; }
    public void setEdital(Edital edital) { this.edital = edital; }

    public boolean isClicou() { return clicou; }
    public void setClicou(boolean clicou) { this.clicou = clicou; }

    public boolean isFavoritou() { return favoritou; }
    public void setFavoritou(boolean favoritou) { this.favoritou = favoritou; }

    public boolean isSeInscreveu() { return seInscreveu; }
    public void setSeInscreveu(boolean seInscreveu) { this.seInscreveu = seInscreveu; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}