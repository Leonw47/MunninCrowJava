package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.PerfilUpdateRequest;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User registrar(User user) {
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

    public User atualizarPerfil(User user, PerfilUpdateRequest req) {

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

}