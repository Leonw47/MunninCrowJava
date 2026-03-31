package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUsuarioDestinoIdOrderByDataCriacaoDesc(Long usuarioId);

    List<Notificacao> findByUsuarioDestinoIdAndLidaFalseOrderByDataCriacaoDesc(Long usuarioId);
}