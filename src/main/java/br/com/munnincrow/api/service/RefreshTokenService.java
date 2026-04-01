package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.AuthResponse;
import br.com.munnincrow.api.model.RefreshToken;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.RefreshTokenRepository;
import br.com.munnincrow.api.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repo;
    private final JwtUtil jwtUtil;

    public RefreshTokenService(RefreshTokenRepository repo, JwtUtil jwtUtil) {
        this.repo = repo;
        this.jwtUtil = jwtUtil;
    }

    public RefreshToken criar(User user) {

        RefreshToken rt = new RefreshToken();
        rt.setToken(UUID.randomUUID().toString());
        rt.setUser(user);
        rt.setExpiracao(LocalDateTime.now().plusDays(7));
        rt.setRevogado(false);

        return repo.save(rt);
    }

    public AuthResponse renovar(String refreshToken) {

        RefreshToken rt = repo.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido."));

        if (rt.isRevogado() || rt.expirado()) {
            repo.delete(rt);
            throw new IllegalArgumentException("Refresh token expirado ou revogado.");
        }

        User user = rt.getUser();

        // Revoga o token antigo
        rt.setRevogado(true);
        repo.save(rt);

        // Gera novos tokens
        String novoAccessToken = jwtUtil.gerarToken(user);
        RefreshToken novoRefreshToken = criar(user);

        return new AuthResponse(novoAccessToken, novoRefreshToken.getToken());
    }

    public void revogar(String refreshToken) {
        repo.findByToken(refreshToken).ifPresent(rt -> {
            rt.setRevogado(true);
            repo.save(rt);
        });
    }

    public void revogarTodos(User user) {
        repo.findAllByUser(user).forEach(rt -> {
            rt.setRevogado(true);
            repo.save(rt);
        });
    }
}