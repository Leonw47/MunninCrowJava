package br.com.munnincrow.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao_tutoria")
public class AvaliacaoTutoria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int nota;

    @ManyToOne
    @JoinColumn(name = "consultor_id")
    private User consultor;

    @ManyToOne
    @JoinColumn(name = "empreendedor_id")
    private User empreendedor;

    public int getNota() { return nota; }
    public User getConsultor() { return consultor; }
    public User getEmpreendedor() { return empreendedor; }
}