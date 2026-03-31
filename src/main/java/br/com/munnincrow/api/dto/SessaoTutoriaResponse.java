package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.enums.StatusSessao;
import java.time.LocalDateTime;

public class SessaoTutoriaResponse {
    public Long id;
    public Long solicitacaoId;
    public Long consultorId;
    public Long empreendedorId;
    public LocalDateTime inicio;
    public LocalDateTime fim;
    public StatusSessao status;
    public String observacoes;
}
