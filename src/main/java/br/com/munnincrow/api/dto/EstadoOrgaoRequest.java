package br.com.munnincrow.api.dto;

import jakarta.validation.constraints.NotBlank;

public class EstadoOrgaoRequest {

    @NotBlank(message = "O estado é obrigatório")
    public String estado;

    @NotBlank(message = "O órgão é obrigatório")
    public String orgao;
}