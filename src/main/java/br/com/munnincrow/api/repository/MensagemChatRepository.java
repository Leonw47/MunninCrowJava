package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.MensagemChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemChatRepository extends JpaRepository<MensagemChat, Long> {

    // Mensagens raiz de um edital (threads principais)
    Page<MensagemChat> findByEditalIdAndMensagemPaiIsNullOrderByDataEnvioDesc(Long editalId, Pageable pageable);

    // Respostas de uma mensagem
    List<MensagemChat> findByMensagemPaiIdOrderByDataEnvioAsc(Long mensagemPaiId);

    // Todas as mensagens de um edital (se precisar)
    Page<MensagemChat> findByEditalId(Long editalId, Pageable pageable);
}