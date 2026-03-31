package br.com.munnincrow.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mensagem_chat")
public class MensagemChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String conteudo;

    @Column(nullable = false)
    private LocalDateTime dataEnvio = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private User autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edital_id", nullable = false)
    private Edital edital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mensagem_pai_id")
    private MensagemChat mensagemPai;

    @OneToMany(mappedBy = "mensagemPai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MensagemChat> respostas = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }

    public User getAutor() { return autor; }
    public void setAutor(User autor) { this.autor = autor; }

    public Edital getEdital() { return edital; }
    public void setEdital(Edital edital) { this.edital = edital; }

    public MensagemChat getMensagemPai() { return mensagemPai; }
    public void setMensagemPai(MensagemChat mensagemPai) { this.mensagemPai = mensagemPai; }

    public List<MensagemChat> getRespostas() { return respostas; }
    public void setRespostas(List<MensagemChat> respostas) { this.respostas = respostas; }
}