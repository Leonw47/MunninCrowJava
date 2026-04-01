package br.com.munnincrow.api.service;

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
        rt.setExpiracao(LocalDateTime.now().plusDays(7)); // 7 dias

        return repo.save(rt);
    }

    public String renovar(String refreshToken) {

        RefreshToken rt = repo.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido."));

        if (rt.expirado()) {
            repo.delete(rt);
            throw new IllegalArgumentException("Refresh token expirado.");
        }

        User user = rt.getUser();

        return jwtUtil.gerarToken(user);
    }

    public void revogar(String refreshToken) {
        repo.findByToken(refreshToken).ifPresent(repo::delete);
    }
}