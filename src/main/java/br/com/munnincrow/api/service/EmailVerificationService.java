package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.EmailVerificationToken;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.EmailVerificationTokenRepository;
import br.com.munnincrow.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepo;
    private final UserRepository userRepo;
    private final EmailNotificationService emailService;

    public EmailVerificationService(EmailVerificationTokenRepository tokenRepo,
                                    UserRepository userRepo,
                                    EmailNotificationService emailService) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    public void enviarTokenVerificacao(User user) {

        String token = UUID.randomUUID().toString();

        EmailVerificationToken evt = new EmailVerificationToken();
        evt.setToken(token);
        evt.setUser(user);
        evt.setExpiracao(LocalDateTime.now().plusHours(24));

        tokenRepo.save(evt);

        String link = "https://seusite.com/verify-email?token=" + token;

        emailService.enviar(
                user.getEmail(),
                "Verifique seu e-mail",
                "Clique no link para verificar sua conta:\n" + link
        );
    }

    public void verificarEmail(String token) {

        EmailVerificationToken evt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido."));

        if (evt.expirado()) {
            throw new IllegalArgumentException("Token expirado.");
        }

        User user = evt.getUser();
        user.setEmailVerificado(true);
        userRepo.save(user);

        tokenRepo.delete(evt);
    }
}