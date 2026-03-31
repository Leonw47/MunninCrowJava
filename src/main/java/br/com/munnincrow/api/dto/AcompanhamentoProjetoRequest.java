package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.enums.StatusProjeto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AcompanhamentoProjetoRequest {

    @NotBlank(message = "O título é obrigatório")
    public String titulo;

    @NotNull(message = "A data de início é obrigatória")
    public LocalDate dataInicio;

    public LocalDate dataFim;

    @NotNull(message = "O status é obrigatório")
    public StatusProjeto status;

    @NotNull(message = "O ID do usuário criador é obrigatório")
    public Long criadoPorId;
}