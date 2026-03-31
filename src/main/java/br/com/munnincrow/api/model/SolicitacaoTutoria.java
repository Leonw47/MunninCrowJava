package br.com.munnincrow.api.model;

import br.com.munnincrow.api.model.enums.StatusSolicitacaoTutoria;
import br.com.munnincrow.api.model.enums.TipoSolicitacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao_tutoria")
public class SolicitacaoTutoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSolicitacao tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacaoTutoria status = StatusSolicitacaoTutoria.ABERTA;

    @Column(length = 2000)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empreendedor_id", nullable = false)
    private User empreendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultor_id")
    private User consultor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column
    private LocalDateTime dataExpiracao;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        this.dataExpiracao = LocalDateTime.now().plusDays(7);
    }

    public Long getId() { return id; }
    public TipoSolicitacao getTipo() { return tipo; }
    public void setTipo(TipoSolicitacao tipo) { this.tipo = tipo; }
    public StatusSolicitacaoTutoria getStatus() { return status; }
    public void setStatus(StatusSolicitacaoTutoria status) { this.status = status; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public User getEmpreendedor() { return empreendedor; }
    public void setEmpreendedor(User empreendedor) { this.empreendedor = empreendedor; }
    public User getConsultor() { return consultor; }
    public void setConsultor(User consultor) { this.consultor = consultor; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataExpiracao() { return dataExpiracao; }
    public void setDataExpiracao(LocalDateTime dataExpiracao) { this.dataExpiracao = dataExpiracao; }
}