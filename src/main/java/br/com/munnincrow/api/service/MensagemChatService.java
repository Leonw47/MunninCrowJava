package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.MensagemChat;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.EditalRepository;
import br.com.munnincrow.api.repository.MensagemChatRepository;
import br.com.munnincrow.api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagemChatService {

    private final MensagemChatRepository repository;
    private final EditalRepository editalRepository;
    private final UserRepository userRepository;

    public MensagemChatService(
            MensagemChatRepository repository,
            EditalRepository editalRepository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.editalRepository = editalRepository;
        this.userRepository = userRepository;
    }

    // Lista mensagens raiz (threads principais) de um edital
    public Page<MensagemChat> listarPorEdital(Long editalId, Pageable pageable) {
        return repository.findByEditalIdAndMensagemPaiIsNullOrderByDataEnvioDesc(editalId, pageable);
    }

    // Lista respostas de uma mensagem
    public List<MensagemChat> listarRespostas(Long mensagemId) {
        return repository.findByMensagemPaiIdOrderByDataEnvioAsc(mensagemId);
    }

    // Cria mensagem ou resposta
    public MensagemChat salvar(MensagemChat mensagem, Long editalId, Long autorId, Long mensagemPaiId) {

        Edital edital = editalRepository.findById(editalId).orElse(null);
        if (edital == null) {
            throw new IllegalArgumentException("Edital não encontrado.");
        }

        User autor = userRepository.findById(autorId).orElse(null);
        if (autor == null) {
            throw new IllegalArgumentException("Autor não encontrado.");
        }

        mensagem.setEdital(edital);
        mensagem.setAutor(autor);

        if (mensagemPaiId != null) {
            MensagemChat pai = repository.findById(mensagemPaiId).orElse(null);
            if (pai == null) {
                throw new IllegalArgumentException("Mensagem pai não encontrada.");
            }
            mensagem.setMensagemPai(pai);
        }

        return repository.save(mensagem);
    }

    // Exclui mensagem e suas respostas
    public boolean deletar(Long id) {
        MensagemChat mensagem = repository.findById(id).orElse(null);
        if (mensagem == null) return false;

        deletarRecursivo(mensagem);
        return true;
    }

    private void deletarRecursivo(MensagemChat mensagem) {
        List<MensagemChat> respostas = repository.findByMensagemPaiIdOrderByDataEnvioAsc(mensagem.getId());
        for (MensagemChat resposta : respostas) {
            deletarRecursivo(resposta);
        }
        repository.delete(mensagem);
    }
}