package br.com.munnincrow.api.model;

import br.com.munnincrow.api.model.enums.StatusSessao;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessao_tutoria")
public class SessaoTutoria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consultor_id")
    private User consultor;

    @ManyToOne
    @JoinColumn(name = "empreendedor_id")
    private User empreendedor;

    @Enumerated(EnumType.STRING)
    private StatusSessao status;

    public User getConsultor() { return consultor; }
    public User getEmpreendedor() { return empreendedor; }
    public StatusSessao getStatus() { return status; }
}