package br.com.munnincrow.api.jobs;

import br.com.munnincrow.api.model.enums.StatusSolicitacaoTutoria;
import br.com.munnincrow.api.service.SolicitacaoTutoriaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExpiracaoSolicitacaoJob {

    private final SolicitacaoTutoriaService solicitacaoService;

    public ExpiracaoSolicitacaoJob(SolicitacaoTutoriaService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    // Executa todos os dias às 02:00 da manhã
    @Scheduled(cron = "0 0 2 * * *")
    public void expirarSolicitacoes() {
        solicitacaoService.expirarSolicitacoesVencidas(LocalDateTime.now());
    }
}