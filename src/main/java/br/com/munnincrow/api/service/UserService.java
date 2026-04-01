package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.PerfilUpdateRequest;
import br.com.munnincrow.api.dto.UpdateProfileRequest;
import br.com.munnincrow.api.dto.UserResponseDTO;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final EmailNotificationService emailService;

    public UserService(UserRepository repo, PasswordEncoder encoder, EmailNotificationService emailService) {
        this.repo = repo;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    public User registrar(User user) {

        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já está em uso.");
        }

        validarSenhaForte(user.getSenhaHash());

        user.setSenhaHash(encoder.encode(user.getSenhaHash()));
        return repo.save(user);
    }

    public User buscarPorEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public User buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public User atualizarPerfil(User user, UpdateProfileRequest req) {

        if (req.nome != null && !req.nome.isBlank()) {
            user.setNome(req.nome);
        }

        if (req.segmento != null) {
            user.setSegmento(req.segmento);
        }

        if (req.maturidade != null) {
            user.setMaturidade(req.maturidade);
        }

        if (req.faturamentoAnual != null) {
            user.setFaturamentoAnual(req.faturamentoAnual);
        }

        return repo.save(user);
    }

    // 🔥 MÉTODO QUE FALTAVA
    public User getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Nenhum usuário autenticado.");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof User user) {
            return buscarPorEmail(user.getEmail());
        }

        throw new IllegalStateException("Usuário autenticado inválido.");
    }

    public UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.id = user.getId();
        dto.nome = user.getNome();
        dto.email = user.getEmail();
        dto.segmento = user.getSegmento();
        dto.maturidade = user.getMaturidade();
        dto.faturamentoAnual = user.getFaturamentoAnual();
        dto.role = user.getRole().name();
        return dto;
    }

    public void validarSenhaForte(String senha) {
        if (senha.length() < 8)
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");

        if (!senha.matches(".*[0-9].*"))
            throw new IllegalArgumentException("A senha deve conter pelo menos um número.");

        if (!senha.matches(".*[a-zA-Z].*"))
            throw new IllegalArgumentException("A senha deve conter pelo menos uma letra.");

        if (!senha.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/].*"))
            throw new IllegalArgumentException("A senha deve conter pelo menos um caractere especial.");
    }

    public User atualizarSenha(User usuario, String senhaAtual, String novaSenha) {

        if (!encoder.matches(senhaAtual, usuario.getSenhaHash())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }

        validarSenhaForte(novaSenha);

        usuario.setSenhaHash(encoder.encode(novaSenha));
        return repo.save(usuario);
    }

    public boolean contaBloqueada(User user) {
        return user.getBloqueadoAte() != null &&
                LocalDateTime.now().isBefore(user.getBloqueadoAte());
    }

    public void registrarFalhaLogin(User user) {

        int novasTentativas = user.getTentativasFalhas() + 1;
        user.setTentativasFalhas(novasTentativas);

        if (novasTentativas >= 5) {
            user.setBloqueadoAte(LocalDateTime.now().plusMinutes(15));

            emailService.enviar(
                    user.getEmail(),
                    "Conta bloqueada temporariamente",
                    "Detectamos várias tentativas de login falhas. Sua conta foi bloqueada por 15 minutos."
            );

        }

        repo.save(user);
    }

    public void limparTentativas(User user) {
        user.setTentativasFalhas(0);
        user.setBloqueadoAte(null);
        repo.save(user);
    }
}