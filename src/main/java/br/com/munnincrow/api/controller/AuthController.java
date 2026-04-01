package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.*;
import br.com.munnincrow.api.model.RefreshToken;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.security.JwtUtil;
import br.com.munnincrow.api.service.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetService passwordResetService;
    private final EmailChangeService emailChangeService;
    private final EmailVerificationService emailVerificationService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            UserService userService,
            PasswordEncoder encoder,
            JwtUtil jwtUtil,
            PasswordResetService passwordResetService,
            EmailChangeService emailChangeService,
            EmailVerificationService emailVerificationService,
            RefreshTokenService refreshTokenService,
            AuthenticationManager authenticationManager) {

        this.userService = userService;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.passwordResetService = passwordResetService;
        this.emailChangeService = emailChangeService;
        this.emailVerificationService = emailVerificationService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public AuthResponse registrar(@RequestBody RegisterRequest req) {

        User novo = new User();
        novo.setNome(req.nome);
        novo.setEmail(req.email);
        novo.setSenhaHash(encoder.encode(req.senha));

        User salvo = userService.registrar(novo);

        emailVerificationService.enviarTokenVerificacao(salvo);

        String accessToken = jwtUtil.gerarToken(salvo);
        RefreshToken refreshToken = refreshTokenService.criar(salvo);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }


    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email, req.senha)
        );

        User user = userService.buscarPorEmail(req.email);

        String accessToken = jwtUtil.gerarToken(user);
        RefreshToken refreshToken = refreshTokenService.criar(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @GetMapping("/me")
    public UserResponseDTO me() {
        User usuario = userService.getUsuarioLogado();
        return userService.toDTO(usuario);
    }

    @PutMapping("/change-password")
    public String alterarSenha(@RequestBody UpdatePasswordRequest req) {

        User usuario = userService.getUsuarioLogado();

        userService.atualizarSenha(usuario, req.senhaAtual, req.novaSenha);

        return "Senha atualizada com sucesso.";
    }

    @PostMapping("/forgot-password")
    public String solicitarReset(@RequestBody PasswordResetRequest req) {
        passwordResetService.solicitarReset(req.email);
        return "Se o e-mail existir, enviaremos instruções de recuperação.";
    }

    @PostMapping("/reset-password")
    public String resetarSenha(@RequestBody PasswordResetConfirmRequest req) {
        passwordResetService.redefinirSenha(req.token, req.novaSenha);
        return "Senha redefinida com sucesso.";
    }

    @PutMapping("/me/update")
    public UserResponseDTO atualizarPerfil(@RequestBody UpdateProfileRequest req) {

        User usuario = userService.getUsuarioLogado();

        User atualizado = userService.atualizarPerfil(usuario, req);

        return userService.toDTO(atualizado);
    }

    @PutMapping("/me/change-email")
    public String solicitarTrocaEmail(@RequestBody EmailChangeRequest req) {

        User usuario = userService.getUsuarioLogado();

        emailChangeService.solicitarTrocaEmail(usuario, req.novoEmail);

        return "Se o e-mail informado for válido, enviaremos um link de confirmação.";
    }

    @PostMapping("/confirm-email-change")
    public String confirmarTroca(@RequestBody EmailChangeConfirmRequest req) {

        emailChangeService.confirmarTrocaEmail(req.token);

        return "E-mail atualizado com sucesso.";
    }

    @PostMapping("/verify-email")
    public String verificarEmail(@RequestBody EmailChangeConfirmRequest req) {

        emailVerificationService.verificarEmail(req.token);

        return "E-mail verificado com sucesso.";
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest req) {

        String novoAccessToken = refreshTokenService.renovar(req.refreshToken);

        return new AuthResponse(novoAccessToken, req.refreshToken);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody LogoutRequest req) {

        refreshTokenService.revogar(req.refreshToken);

        return "Logout realizado com sucesso.";
    }
}