package br.com.munnincrow.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MensagemChatResponse {

    public Long id;
    public String conteudo;
    public LocalDateTime dataEnvio;
    public Long autorId;
    public Long editalId;
    public Long mensagemPaiId;
}