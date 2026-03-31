package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.enums.StatusSolicitacao;
import br.com.munnincrow.api.model.enums.StatusSolicitacaoTutoria;
import br.com.munnincrow.api.model.enums.TipoSolicitacao;

import java.time.LocalDateTime;

public class SolicitacaoTutoriaResponse {

    public Long id;
    public TipoSolicitacao tipo;
    public StatusSolicitacaoTutoria status;
    public String descricao;
    public Long empreendedorId;
    public Long consultorId;
    public LocalDateTime dataCriacao;
}