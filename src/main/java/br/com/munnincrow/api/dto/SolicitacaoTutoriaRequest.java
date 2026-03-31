package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.enums.TipoSolicitacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitacaoTutoriaRequest {

    @NotNull(message = "O tipo é obrigatório")
    public TipoSolicitacao tipo;

    @NotBlank(message = "A descrição é obrigatória")
    public String descricao;

    @NotNull(message = "O ID do empreendedor é obrigatório")
    public Long empreendedorId;

    public Long consultorId; // opcional
}