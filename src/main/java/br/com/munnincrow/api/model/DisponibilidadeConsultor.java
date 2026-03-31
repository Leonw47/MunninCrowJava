package br.com.munnincrow.api.model;

import br.com.munnincrow.api.model.enums.DiaSemana;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "disponibilidade_consultor")
public class DisponibilidadeConsultor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultor_id", nullable = false)
    private User consultor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana dia;

    @Column(nullable = false)
    private LocalTime inicio;

    @Column(nullable = false)
    private LocalTime fim;

    // getters e setters

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public User getConsultor() {return consultor;}
    public void setConsultor(User consultor) {this.consultor = consultor;}
    public DiaSemana getDia() {return dia;}
    public void setDia(DiaSemana dia) {this.dia = dia;}
    public LocalTime getInicio() {return inicio;}
    public void setInicio(LocalTime inicio) {this.inicio = inicio;}
    public LocalTime getFim() {return fim;}
    public void setFim(LocalTime fim) {this.fim = fim;}
}