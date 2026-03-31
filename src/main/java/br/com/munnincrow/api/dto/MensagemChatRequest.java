package br.com.munnincrow.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MensagemChatRequest {

    @NotBlank(message = "O conteúdo da mensagem é obrigatório")
    public String conteudo;

    @NotNull(message = "O ID do edital é obrigatório")
    public Long editalId;

    @NotNull(message = "O ID do autor é obrigatório")
    public Long autorId;

    // Se for resposta, vem preenchido; se for mensagem raiz, é null
    public Long mensagemPaiId;
}