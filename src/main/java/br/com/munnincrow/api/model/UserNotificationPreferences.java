package br.com.munnincrow.api.model;

import jakarta.persistence.*;

@Entity
public class UserNotificationPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private User usuario;

    private boolean receberEmail = true;
    private boolean receberPush = true;
    private boolean apenasCriticos = false;
    private boolean resumoDiario = false;

    public Long getId() { return id; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public boolean isReceberEmail() { return receberEmail; }
    public void setReceberEmail(boolean receberEmail) { this.receberEmail = receberEmail; }

    public boolean isReceberPush() { return receberPush; }
    public void setReceberPush(boolean receberPush) { this.receberPush = receberPush; }

    public boolean isApenasCriticos() { return apenasCriticos; }
    public void setApenasCriticos(boolean apenasCriticos) { this.apenasCriticos = apenasCriticos; }

    public boolean isResumoDiario() { return resumoDiario; }
    public void setResumoDiario(boolean resumoDiario) { this.resumoDiario = resumoDiario; }
}