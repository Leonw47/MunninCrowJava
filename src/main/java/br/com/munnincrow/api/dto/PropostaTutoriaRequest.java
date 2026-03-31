package br.com.munnincrow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PropostaTutoriaRequest {
    @NotNull
    public Double valor;

    @NotBlank
    public String descricao;

    @NotNull
    public Long autorId;

    @NotNull
    public Long solicitacaoId;
}
