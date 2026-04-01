package br.com.munnincrow.api.scheduler;

import br.com.munnincrow.api.service.AlertaPropostaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlertaScheduler {

    private final AlertaPropostaService alertaService;

    public AlertaScheduler(AlertaPropostaService alertaService) {
        this.alertaService = alertaService;
    }

    @Scheduled(cron = "0 0 7 * * *") // todos os dias às 07:00
    public void executarVerificacao() {
        alertaService.verificarPrazos();
    }
}