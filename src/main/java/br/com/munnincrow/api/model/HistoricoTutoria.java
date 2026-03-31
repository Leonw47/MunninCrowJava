package br.com.munnincrow.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_tutoria")
public class HistoricoTutoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private SolicitacaoTutoria solicitacao;

    @Column(nullable = false, length = 2000)
    private String mensagem;

    @Column(nullable = false)
    private LocalDateTime data = LocalDateTime.now();

    @Column(nullable = false)
    private Long usuarioId;

    // getters e setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SolicitacaoTutoria getSolicitacao() { return solicitacao; }
    public void setSolicitacao(SolicitacaoTutoria solicitacao) { this.solicitacao = solicitacao; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}