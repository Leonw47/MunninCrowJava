package br.com.munnincrow.api.service;


import br.com.munnincrow.api.model.EmailChangeToken;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.EmailChangeTokenRepository;
import br.com.munnincrow.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailChangeService {

    private final UserRepository userRepo;
    private final EmailChangeTokenRepository tokenRepo;
    private final EmailNotificationService emailService;

    public EmailChangeService(UserRepository userRepo,
                              EmailChangeTokenRepository tokenRepo,
                              EmailNotificationService emailService) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.emailService = emailService;
    }

    public void solicitarTrocaEmail(User user, String novoEmail) {

        if (userRepo.findByEmail(novoEmail).isPresent()) {
            throw new IllegalArgumentException("E-mail já está em uso.");
        }

        String token = UUID.randomUUID().toString();

        EmailChangeToken ect = new EmailChangeToken();
        ect.setToken(token);
        ect.setUser(user);
        ect.setNovoEmail(novoEmail);
        ect.setExpiracao(LocalDateTime.now().plusHours(1));

        tokenRepo.save(ect);

        String link = "https://seusite.com/confirm-email-change?token=" + token;

        emailService.enviar(
                novoEmail,
                "Confirme a alteração de e-mail",
                "Clique no link para confirmar a troca de e-mail:\n" + link
        );
    }

    public void confirmarTrocaEmail(String token) {

        EmailChangeToken ect = tokenRepo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido."));

        if (ect.expirado()) {
            throw new IllegalArgumentException("Token expirado.");
        }

        User user = ect.getUser();
        user.setEmail(ect.getNovoEmail());
        userRepo.save(user);

        tokenRepo.delete(ect);
    }
}