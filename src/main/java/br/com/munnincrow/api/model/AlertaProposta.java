package br.com.munnincrow.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AlertaProposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Proposta proposta;

    private String tipo; // prazo_proximo, edital_fechando, status_atualizado

    private String mensagem;

    private LocalDateTime dataGeracao;

    private boolean lido;

    public Long getId() { return id; }
    public Proposta getProposta() { return proposta; }
    public void setProposta(Proposta proposta) { this.proposta = proposta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public LocalDateTime getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDateTime dataGeracao) { this.dataGeracao = dataGeracao; }

    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }
}