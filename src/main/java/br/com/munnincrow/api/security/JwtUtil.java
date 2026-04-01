package br.com.munnincrow.api.security;

import br.com.munnincrow.api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.Duration;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey SECRET = Keys.hmacShaKeyFor(
            "sua_chave_secreta_segura_sua_chave_secreta_segura".getBytes()
    );

    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    public Claims extrairClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean tokenValido(String token, User user) {
        String email = extrairEmail(token);
        return email.equals(user.getEmail()) && !expirado(token);
    }

    private boolean expirado(String token) {
        Date exp = extrairClaims(token).getExpiration();
        return exp.before(new Date());
    }

    public String gerarToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer("munnincrow-api")
                .setAudience("munnincrow-client")
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(10))))
                .signWith(SECRET, SignatureAlgorithm.HS256)
                .compact();
    }
}