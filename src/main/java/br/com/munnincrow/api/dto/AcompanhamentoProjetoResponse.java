package br.com.munnincrow.api.dto;

import br.com.munnincrow.api.model.enums.StatusProjeto;
import java.time.LocalDate;

public class AcompanhamentoProjetoResponse {

    public Long id;
    public String titulo;
    public LocalDate dataInicio;
    public LocalDate dataFim;
    public StatusProjeto status;
    public Long criadoPorId;
}