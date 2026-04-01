package br.com.munnincrow.api.security;

import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder encoder;

    public CustomAuthenticationProvider(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String email = authentication.getName();
        String senha = authentication.getCredentials().toString();

        User user = userService.buscarPorEmail(email);

        if (user == null) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        if (userService.contaBloqueada(user)) {
            throw new BadCredentialsException("Conta temporariamente bloqueada.");
        }

        if (!encoder.matches(senha, user.getSenhaHash())) {
            userService.registrarFalhaLogin(user);
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        userService.limparTentativas(user);

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}