package br.com.munnincrow.api.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "estado_orgao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"estado", "orgao"})
)
public class EstadoOrgao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(nullable = false)
    private String orgao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        this.estado = estado != null ? estado.trim().toUpperCase() : null;
    }

    public String getOrgao() { return orgao; }
    public void setOrgao(String orgao) {
        this.orgao = orgao != null ? orgao.trim() : null;
    }

    @Override
    public String toString() {
        return estado + " - " + orgao;
    }
}