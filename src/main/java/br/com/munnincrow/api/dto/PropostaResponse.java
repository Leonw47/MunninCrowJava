package br.com.munnincrow.api.dto;

import java.time.LocalDate;
import java.util.List;

public class PropostaResponse {
    public Long id;
    public Long editalId;
    public String editalTitulo;
    public String titulo;
    public String status;
    public LocalDate dataCriacao;
    public LocalDate dataAtualizacao;
    public List<CampoPropostaResponse> campos;
}