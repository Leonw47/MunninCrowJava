package br.com.munnincrow.api.scheduler;

import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.UserRepository;
import br.com.munnincrow.api.service.NotificacaoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoScheduler {

    private final UserRepository userRepo;
    private final NotificacaoService notificacaoService;

    public NotificacaoScheduler(UserRepository userRepo, NotificacaoService notificacaoService) {
        this.userRepo = userRepo;
        this.notificacaoService = notificacaoService;
    }

    @Scheduled(cron = "0 0 8 * * *") // todos os dias às 08:00
    public void gerarNotificacoesDiarias() {
        for (User u : userRepo.findAll()) {
            notificacaoService.gerarNotificacoes(u);
        }
    }
}