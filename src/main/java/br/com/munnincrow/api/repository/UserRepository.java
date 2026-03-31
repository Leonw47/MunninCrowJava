package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByTipoUsuarioOrderByMediaAvaliacoesDesc(String tipoUsuario);
}