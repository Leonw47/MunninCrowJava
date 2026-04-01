package br.com.munnincrow.api.model;

import br.com.munnincrow.api.model.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean emailVerificado = false;

    @Column(nullable = false)
    private String senhaHash;

    @Column(nullable = false)
    private int tentativasFalhas = 0;

    private LocalDateTime bloqueadoAte;

    @Column(nullable = false)
    private String nome;

    private String segmento;

    private String maturidade;

    private Double faturamentoAnual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    private LocalDate dataCadastro = LocalDate.now();

    public Long getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isEmailVerificado() {
        return emailVerificado;
    }
    public void setEmailVerificado(boolean emailVerificado) {
        this.emailVerificado = emailVerificado;
    }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSegmento() { return segmento; }
    public void setSegmento(String segmento) { this.segmento = segmento; }

    public String getMaturidade() { return maturidade; }
    public void setMaturidade(String maturidade) { this.maturidade = maturidade; }

    public Double getFaturamentoAnual() { return faturamentoAnual; }
    public void setFaturamentoAnual(Double faturamentoAnual) { this.faturamentoAnual = faturamentoAnual; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }

    public int getTentativasFalhas() {
        return tentativasFalhas;
    }
    public void setTentativasFalhas(int tentativasFalhas) {
        this.tentativasFalhas = tentativasFalhas;
    }
    public LocalDateTime getBloqueadoAte() {
        return bloqueadoAte;
    }
    public void setBloqueadoAte(LocalDateTime bloqueadoAte) {
        this.bloqueadoAte = bloqueadoAte;
    }
}