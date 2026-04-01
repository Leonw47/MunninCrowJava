package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.PasswordResetToken;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.PasswordResetTokenRepository;
import br.com.munnincrow.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final EmailNotificationService emailService;

    public PasswordResetService(UserRepository userRepo,
                                PasswordResetTokenRepository tokenRepo,
                                PasswordEncoder encoder,
                                UserService userService,
                                EmailNotificationService emailService) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.encoder = encoder;
        this.userService = userService;
        this.emailService = emailService;
    }

    public void solicitarReset(String email) {

        userRepo.findByEmail(email).ifPresent(user -> {

            String token = UUID.randomUUID().toString();

            PasswordResetToken prt = new PasswordResetToken();
            prt.setToken(token);
            prt.setUser(user);
            prt.setExpiracao(LocalDateTime.now().plusHours(1));

            tokenRepo.save(prt);

            String link = "https://seusite.com/reset-password?token=" + token;

            emailService.enviar(
                    user.getEmail(),
                    "Recuperação de senha",
                    "Clique no link para redefinir sua senha:\n" + link
            );
        });
    }

    public void redefinirSenha(String token, String novaSenha) {

        PasswordResetToken prt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido."));

        if (prt.expirado()) {
            throw new IllegalArgumentException("Token expirado.");
        }

        User user = prt.getUser();

        userService.validarSenhaForte(novaSenha);

        user.setSenhaHash(encoder.encode(novaSenha));
        userRepo.save(user);

        tokenRepo.delete(prt);
    }
}