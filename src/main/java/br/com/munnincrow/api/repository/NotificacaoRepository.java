package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.Notificacao;
import br.com.munnincrow.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUsuarioOrderByDataCriacaoDesc(User usuario);

    long countByUsuarioAndLidaFalse(User usuario);
}