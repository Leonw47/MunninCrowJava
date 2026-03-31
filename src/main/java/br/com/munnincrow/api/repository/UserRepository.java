package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByNomeContainingIgnoreCase(String nome);
    List<User> findByTipoUsuarioOrderByMediaAvaliacoesDesc(String tipo);
}