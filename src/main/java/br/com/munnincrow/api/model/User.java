package br.com.munnincrow.api.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // CONSULTOR ou EMPREENDEDOR
    private String tipoUsuario;

    private Double mediaAvaliacoes = 0.0;

    private Integer totalAvaliacoes = 0;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getTipoUsuario() { return tipoUsuario; }
    public Double getMediaAvaliacoes() { return mediaAvaliacoes; }
    public Integer getTotalAvaliacoes() { return totalAvaliacoes; }
}
