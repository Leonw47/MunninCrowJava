package br.com.munnincrow.api.model;

import br.com.munnincrow.api.model.enums.StatusPagamento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento_tutoria")
public class PagamentoTutoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposta_id", nullable = false)
    private PropostaTutoria proposta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empreendedor_id", nullable = false)
    private User empreendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultor_id", nullable = false)
    private User consultor;

    @Column(nullable = false)
    private double valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    private LocalDateTime dataCriacao = LocalDateTime.now();
    private LocalDateTime dataConfirmacao;

    private String codigoTransacao; // para gateway real

    // getters e setters

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public PropostaTutoria getProposta() {return proposta;}
    public void setProposta(PropostaTutoria proposta) {this.proposta = proposta;}
    public User getEmpreendedor() {return empreendedor;}
    public void setEmpreendedor(User empreendedor) {this.empreendedor = empreendedor;}
    public User getConsultor() {return consultor;}
    public void setConsultor(User consultor) {this.consultor = consultor;}
    public double getValor() {return valor;}
    public void setValor(double valor) {this.valor = valor;}
    public StatusPagamento getStatus() {return status;}
    public void setStatus(StatusPagamento status) {this.status = status;}
    public LocalDateTime getDataCriacao() {return dataCriacao;}
    public void setDataCriacao(LocalDateTime dataCriacao) {this.dataCriacao = dataCriacao;}
    public LocalDateTime getDataConfirmacao() {return dataConfirmacao;}
    public void setDataConfirmacao(LocalDateTime dataConfirmacao) {this.dataConfirmacao = dataConfirmacao;}
    public String getCodigoTransacao() {return codigoTransacao;}
    public void setCodigoTransacao(String codigoTransacao) {this.codigoTransacao = codigoTransacao;}
}