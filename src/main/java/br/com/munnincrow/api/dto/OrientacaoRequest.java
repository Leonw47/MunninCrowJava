package br.com.munnincrow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrientacaoRequest {

    @NotBlank(message = "O título é obrigatório")
    public String titulo;

    @NotBlank(message = "O tipo é obrigatório")
    public String tipo;

    @NotBlank(message = "A URL é obrigatória")
    public String url;

    @NotNull(message = "O ID do edital é obrigatório")
    public Long editalId;
}