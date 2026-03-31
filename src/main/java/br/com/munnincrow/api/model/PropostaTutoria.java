package br.com.munnincrow.api.model;

import br.com.munnincrow.api.model.enums.StatusPropostaTutoria;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "proposta_tutoria")
public class PropostaTutoria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private User autor; // normalmente o empreendedor

    @ManyToOne
    @JoinColumn(name = "consultor_id")
    private User consultor;

    @Enumerated(EnumType.STRING)
    private StatusPropostaTutoria status;

    public User getAutor() { return autor; }
    public User getConsultor() { return consultor; }
    public StatusPropostaTutoria getStatus() { return status; }
}