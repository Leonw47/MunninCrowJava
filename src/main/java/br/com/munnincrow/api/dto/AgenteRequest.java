package br.com.munnincrow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AgenteRequest {

    @NotBlank(message = "O nome é obrigatório")
    public String nome;

    @NotBlank(message = "O tipo é obrigatório")
    public String tipo;

    @Size(max = 2000, message = "A descrição pode ter no máximo 2000 caracteres")
    public String descricao;
}