package br.com.munnincrow.api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "indisponibilidade_consultor")
public class IndisponibilidadeConsultor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultor_id", nullable = false)
    private User consultor;

    @Column(nullable = false)
    private LocalDate inicio;

    @Column(nullable = false)
    private LocalDate fim;

    private String motivo;

    // getters e setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public User getConsultor() {return consultor;}
    public void setConsultor(User consultor) {this.consultor = consultor;}
    public LocalDate getInicio() {return inicio;}
    public void setInicio(LocalDate inicio) {this.inicio = inicio;}
    public LocalDate getFim() {return fim;}
    public void setFim(LocalDate fim) {this.fim = fim;}
    public String getMotivo() {return motivo;}
    public void setMotivo(String motivo) {this.motivo = motivo;}
}