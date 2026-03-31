package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.AuthResponse;
import br.com.munnincrow.api.dto.LoginRequest;
import br.com.munnincrow.api.dto.RegisterRequest;
import br.com.munnincrow.api.model.enums.Role;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.security.JwtUtil;
import br.com.munnincrow.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        User user = new User();
        user.setNome(req.nome);
        user.setEmail(req.email);
        user.setSenhaHash(req.senha);
        user.setSegmento(req.segmento);
        user.setMaturidade(req.maturidade);
        user.setFaturamentoAnual(req.faturamentoAnual);
        user.setRole(Role.USER);

        User salvo = userService.registrar(user);
        String token = jwtUtil.gerarToken(salvo);

        AuthResponse resp = new AuthResponse();
        resp.token = token;
        resp.nome = salvo.getNome();
        resp.email = salvo.getEmail();
        resp.role = salvo.getRole().name();

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(req.email, req.senha);
            authManager.authenticate(authToken);

            User user = userService.buscarPorEmail(req.email);
            String token = jwtUtil.gerarToken(user);

            AuthResponse resp = new AuthResponse();
            resp.token = token;
            resp.nome = user.getNome();
            resp.email = user.getEmail();
            resp.role = user.getRole().name();

            return ResponseEntity.ok(resp);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }
}