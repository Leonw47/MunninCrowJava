package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.Notificacao;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository repository;
    private final UserService userService;

    public NotificacaoService(NotificacaoRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public void criar(Long usuarioId, String mensagem) {
        User usuario = userService.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        Notificacao n = new Notificacao();
        n.setUsuarioDestino(usuario);
        n.setMensagem(mensagem);
        repository.save(n);
    }

    public List<Notificacao> listar(Long usuarioId) {
        return repository.findByUsuarioDestinoIdOrderByDataCriacaoDesc(usuarioId);
    }

    public List<Notificacao> listarNaoLidas(Long usuarioId) {
        return repository.findByUsuarioDestinoIdAndLidaFalseOrderByDataCriacaoDesc(usuarioId);
    }

    public Notificacao marcarComoLida(Long id) {
        return repository.findById(id).map(n -> {
            n.setLida(true);
            return repository.save(n);
        }).orElse(null);
    }

    public void marcarTodasComoLidas(Long usuarioId) {
        List<Notificacao> notificacoes = repository.findByUsuarioDestinoIdAndLidaFalseOrderByDataCriacaoDesc(usuarioId);
        notificacoes.forEach(n -> n.setLida(true));
        repository.saveAll(notificacoes);
    }
}