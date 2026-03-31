package br.com.munnincrow.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "gasto_projeto")
public class GastoProjeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private LocalDate data;

    @Column(length = 2000)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acompanhamento_id", nullable = false)
    private AcompanhamentoProjeto acompanhamentoProjeto;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public AcompanhamentoProjeto getAcompanhamentoProjeto() { return acompanhamentoProjeto; }
    public void setAcompanhamentoProjeto(AcompanhamentoProjeto acompanhamentoProjeto) { this.acompanhamentoProjeto = acompanhamentoProjeto; }
}