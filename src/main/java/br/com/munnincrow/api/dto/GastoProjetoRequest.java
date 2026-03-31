package br.com.munnincrow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class GastoProjetoRequest {

    @NotBlank(message = "A categoria é obrigatória")
    public String categoria;

    @NotNull(message = "O valor é obrigatório")
    public Double valor;

    @NotNull(message = "A data é obrigatória")
    public LocalDate data;

    public String descricao;

    @NotNull(message = "O ID do acompanhamento é obrigatório")
    public Long acompanhamentoId;
}