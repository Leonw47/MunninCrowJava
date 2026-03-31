package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.MensagemTutoria;
import br.com.munnincrow.api.model.SolicitacaoTutoria;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.MensagemTutoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MensagemTutoriaService {

    private final MensagemTutoriaRepository repository;
    private final SolicitacaoTutoriaService solicitacaoService;
    private final UserService userService;
    private final NotificacaoService notificacaoService;
    private final HistoricoTutoriaService historicoService;

    public MensagemTutoriaService(
            MensagemTutoriaRepository repository,
            SolicitacaoTutoriaService solicitacaoService,
            UserService userService,
            NotificacaoService notificacaoService,
            HistoricoTutoriaService historicoService
    ) {
        this.repository = repository;
        this.solicitacaoService = solicitacaoService;
        this.userService = userService;
        this.notificacaoService = notificacaoService;
        this.historicoService = historicoService;
    }

    public MensagemTutoria enviar(Long solicitacaoId, Long autorId, String conteudo) {

        SolicitacaoTutoria solicitacao = solicitacaoService.buscarPorId(solicitacaoId);
        if (solicitacao == null) throw new IllegalArgumentException("Solicitação não encontrada.");

        User autor = userService.buscarPorId(autorId);
        if (autor == null) throw new IllegalArgumentException("Autor não encontrado.");

        MensagemTutoria msg = new MensagemTutoria();
        msg.setSolicitacao(solicitacao);
        msg.setAutor(autor);
        msg.setConteudo(conteudo);

        MensagemTutoria salva = repository.save(msg);

        // Notificar o destinatário
        Long destinatarioId = autorId.equals(solicitacao.getEmpreendedor().getId())
                ? solicitacao.getConsultor().getId()
                : solicitacao.getEmpreendedor().getId();

        notificacaoService.criar(
                destinatarioId,
                "Nova mensagem na solicitação #" + solicitacao.getId()
        );

        // Registrar histórico
        historicoService.registrar(
                solicitacao,
                autorId,
                "Mensagem enviada na negociação."
        );

        return salva;
    }

    public Page<MensagemTutoria> listar(Long solicitacaoId, Pageable pageable) {
        return repository.findBySolicitacaoIdOrderByDataCriacaoAsc(solicitacaoId, pageable);
    }

    public void marcarComoLida(Long mensagemId) {
        repository.findById(mensagemId).ifPresent(m -> {
            m.setLida(true);
            repository.save(m);
        });
    }
}